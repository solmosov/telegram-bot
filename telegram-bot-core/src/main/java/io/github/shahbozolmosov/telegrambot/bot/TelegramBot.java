package io.github.shahbozolmosov.telegrambot.bot;

import io.github.shahbozolmosov.telegrambot.authorization.AuthorizationManager;
import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.dispatcher.CallbackQueryUpdateDispatcher;
import io.github.shahbozolmosov.telegrambot.dispatcher.Dispatcher;
import io.github.shahbozolmosov.telegrambot.dispatcher.MessageUpdateDispatcher;
import io.github.shahbozolmosov.telegrambot.dispatcher.UpdateTypeDispatcher;
import io.github.shahbozolmosov.telegrambot.dispatcher.resolver.*;
import io.github.shahbozolmosov.telegrambot.scanner.resolver.*;
import io.github.shahbozolmosov.telegrambot.dispatcher.resolver.*;
import io.github.shahbozolmosov.telegrambot.scanner.resolver.*;
import io.github.shahbozolmosov.telegrambot.exception.TelegramBotException;
import io.github.shahbozolmosov.telegrambot.json.ObjectMapperFactory;
import io.github.shahbozolmosov.telegrambot.registry.Registry;
import io.github.shahbozolmosov.telegrambot.scanner.ApplicationPackageResolver;
import io.github.shahbozolmosov.telegrambot.scanner.ClassInstanceFactory;
import io.github.shahbozolmosov.telegrambot.scanner.ClassScanner;
import io.github.shahbozolmosov.telegrambot.scanner.HandlerRegistrar;
import io.github.shahbozolmosov.telegrambot.source.polling.PollingUpdateSource;
import io.github.shahbozolmosov.telegrambot.source.UpdateSource;
import io.github.shahbozolmosov.telegrambot.source.webhook.WebhookUpdateSource;
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

    private boolean started;

    private final ExecutionMode executionMode;

    public TelegramBot(String name, String botToken) {
        this(name, botToken, TelegramBotConfig.defaults());
    }

    public TelegramBot(String name, String botToken, TelegramBotConfig config) {
        this.name = name;

        this.executionMode = config.getExecutionMode();

        // Object Mapper
        this.jsonMapper = ObjectMapperFactory.create();

        this.telegramClient = new TelegramClient(botToken, jsonMapper);

        // Registry
        final Registry registry = new Registry();

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
                new RequestUsersHandlerResolver(),

                new MessageAnnotationResolver(),

                // Callback
                new CallbackHandlerResolver(),

                // Update
                new UpdateHandlerResolver()
        );

        this.handlerRegistrar = new HandlerRegistrar(
                new ClassScanner(),
                new ClassInstanceFactory(),
                registry,
                annotationHandlerResolvers
        );

        // Initialize UpdateSource based on config
        initializeUpdateSource(config);
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
                            executionMode,
                            config.getGlobalExceptionHandler(),
                            config.getProcessingTimeout()
                    );
                    break;
                case WEBHOOK:
                    this.updateSource = new WebhookUpdateSource(
                            name,
                            telegramClient,
                            dispatcher,
                            executionMode,
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

        started = true;

        MDC.put("bot", name);

        try {
            String packageName = new ApplicationPackageResolver().resolve();

            handlerRegistrar.register(packageName);

            updateSource.start();

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
