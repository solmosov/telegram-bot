package io.github.solmosov.telegrambot.bot;

import io.github.solmosov.telegrambot.authorization.AuthorizationManager;
import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.dispatcher.CallbackQueryUpdateDispatcher;
import io.github.solmosov.telegrambot.dispatcher.Dispatcher;
import io.github.solmosov.telegrambot.dispatcher.MessageUpdateDispatcher;
import io.github.solmosov.telegrambot.dispatcher.UpdateTypeDispatcher;
import io.github.solmosov.telegrambot.dispatcher.resolver.*;
import io.github.solmosov.telegrambot.exception.TelegramBotException;
import io.github.solmosov.telegrambot.handler.argument.BotContextArgumentResolver;
import io.github.solmosov.telegrambot.handler.argument.HandlerArgumentResolver;
import io.github.solmosov.telegrambot.handler.argument.HandlerArgumentResolverComposite;
import io.github.solmosov.telegrambot.handler.argument.MessageArgumentResolver;
import io.github.solmosov.telegrambot.json.ObjectMapperFactory;
import io.github.solmosov.telegrambot.registry.Registry;
import io.github.solmosov.telegrambot.scanner.ApplicationPackageResolver;
import io.github.solmosov.telegrambot.scanner.ClassInstanceFactory;
import io.github.solmosov.telegrambot.scanner.ClassScanner;
import io.github.solmosov.telegrambot.scanner.HandlerRegistrar;
import io.github.solmosov.telegrambot.scanner.resolver.*;
import io.github.solmosov.telegrambot.source.UpdateSource;
import io.github.solmosov.telegrambot.source.polling.PollingUpdateSource;
import io.github.solmosov.telegrambot.source.webhook.WebhookUpdateSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

public final class TelegramBot {

    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    private final String name;
    private final TelegramClient telegramClient;
    private final Dispatcher dispatcher;
    private final HandlerRegistrar handlerRegistrar;
    private final JsonMapper jsonMapper;
    private UpdateSource updateSource;
    private ApplicationPackageResolver applicationPackageResolver;

    private boolean started;

    private final TelegramBotConfig config;

    public TelegramBot(String name, String botToken) {
        this(name, botToken, TelegramBotConfig.defaults());
    }

    public TelegramBot(String name, String botToken, TelegramBotConfig config) {
        this(name, botToken, config, null, null, null);
    }

    TelegramBot(String name, String botToken, TelegramBotConfig config, UpdateSource updateSource) {
        this(name, botToken, config, updateSource, null, null);
    }

    TelegramBot(String name, String botToken, TelegramBotConfig config, UpdateSource updateSource, HandlerRegistrar handlerRegistrar) {
        this(name, botToken, config, updateSource, handlerRegistrar, null);
    }

    TelegramBot(
            String name,
            String botToken,
            TelegramBotConfig config,
            UpdateSource updateSource,
            HandlerRegistrar handlerRegistrar,
            ApplicationPackageResolver applicationPackageResolver
    ) {
        this.config = config;

        this.name = name;

        // Object Mapper
        this.jsonMapper = ObjectMapperFactory.create();

        this.telegramClient = new TelegramClient(botToken, jsonMapper);

        // Registry
        final Registry registry = new Registry(name);

        // Authorization Manager
        final AuthorizationManager authorizationManager = new AuthorizationManager(config.getAuthorizationProvider());

        // Message Type Resolvers
        List<MessageTypeResolver> messageTypeResolvers = List.of(
                new CommandMessageTypeResolver(),
                new PhotoMessageTypeResolver(),
                new LocationMessageTypeResolver(),
                new ContactMessageTypeResolver(),
                new RequestUsersMessageTypeResolver()
        );
        FallbackMessageTypeResolver fallbackMessageTypeResolver = new TextMessageTypeResolver();

        // Update dispatchers
        List<UpdateTypeDispatcher> updateTypeDispatchers = List.of(
                new MessageUpdateDispatcher(registry, messageTypeResolvers, fallbackMessageTypeResolver, authorizationManager),
                new CallbackQueryUpdateDispatcher(registry, authorizationManager)
        );

        this.dispatcher = new Dispatcher(name, registry, updateTypeDispatchers, authorizationManager);
        // Annotation Resolvers
        List<HandlerAnnotationResolver> annotationHandlerResolvers = List.of(
                // Message
                new CommandHandlerResolver(),
                new PhotoHandlerResolver(),
                new LocationHandlerResolver(),
                new ContactHandlerResolver(),
                new UsersSharedHandlerResolver(),

                new MessageHandlerResolver(),

                // Callback
                new CallbackQueryHandlerResolver(),

                // Update
                new UpdateHandlerResolver()
        );

        // Handler Registrar
        List<HandlerArgumentResolver> argumentResolvers = List.of(
                new BotContextArgumentResolver(),
                new MessageArgumentResolver()
        );

        HandlerArgumentResolverComposite argumentResolverComposite = new HandlerArgumentResolverComposite(argumentResolvers);

        HandlerRegistrar defaultHandlerRegistrar = new HandlerRegistrar(
                new ClassScanner(),
                new ClassInstanceFactory(),
                registry,
                annotationHandlerResolvers,
                argumentResolverComposite,
                name
        );

        this.handlerRegistrar = handlerRegistrar != null
                ? handlerRegistrar
                : defaultHandlerRegistrar;

        this.applicationPackageResolver = applicationPackageResolver != null
                ? applicationPackageResolver
                : new ApplicationPackageResolver();

        // Initialize UpdateSource based on config
        if (updateSource != null) {
            this.updateSource = updateSource;
        } else {
            initializeUpdateSource(config);
        }
    }

    public void registerHandler(Object handler) {
        handlerRegistrar.register(handler);
    }

    private void initializeUpdateSource(TelegramBotConfig config) {
        MDC.put("bot", name);
        log.info("Bot initializing...");
        try {


            switch (config.getUpdatesMode()) {
                case POLLING:
                    this.updateSource = new PollingUpdateSource(
                            name,
                            telegramClient,
                            dispatcher,
                            config.getExecutionMode(),
                            config.getGlobalExceptionHandler(),
                            config.getProcessingTimeout()
                    );
                    break;
                case WEBHOOK:
                    this.updateSource = new WebhookUpdateSource(
                            name,
                            telegramClient,
                            dispatcher,
                            config.getExecutionMode(),
                            jsonMapper,
                            config.getWebhookHost(),
                            config.getWebhookPort(),
                            config.getWebhookPath(),
                            config.getWebhookUrl(),
                            config.getWebhookPathSecret(),
                            config.getWebhookSecret(),

                            config.getGlobalExceptionHandler(),
                            config.getProcessingTimeout()
                    );
                    break;
            }
        } finally {
            log.info("Bot initializing completed");
            MDC.remove("bot");
        }
    }


    public String name() {
        return this.name;
    }

    public void start() {
        if (started) {
            throw new TelegramBotException("Bot '%s' already been started".formatted(name));
        }


        MDC.put("bot", name);

        try {
            if (config.getHandlerRegistrationMode() == HandlerRegistrationMode.CLASSPATH_SCAN) {
                String packageName = applicationPackageResolver.resolve();

                handlerRegistrar.register(packageName);
            }

            updateSource.start();

            started = true;

            Runtime.getRuntime().addShutdownHook(new Thread(this::stopBot));

            log.info("Started successfully");
        } finally {
            MDC.remove("bot");
        }
    }

    public void stopBot() {

        MDC.put("bot", name);

        try {
            log.info("Shutdown signal received");

            if (updateSource != null) {
                updateSource.stop();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            if (updateSource != null) {
                updateSource.shutdown();
            }

            telegramClient.shutdown();

            started = false;
            log.info("Bot stopped");

        } finally {
            MDC.remove("bot");
        }
    }
}
