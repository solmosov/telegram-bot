package io.github.shahbozolmosov.bot;

import io.github.shahbozolmosov.authorization.AuthorizationManager;
import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.dispatcher.CallbackQueryUpdateDispatcher;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.dispatcher.MessageUpdateDispatcher;
import io.github.shahbozolmosov.dispatcher.UpdateTypeDispatcher;
import io.github.shahbozolmosov.dispatcher.resolver.*;
import io.github.shahbozolmosov.exception.TelegramBotException;
import io.github.shahbozolmosov.json.ObjectMapperFactory;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.scanner.ApplicationPackageResolver;
import io.github.shahbozolmosov.scanner.ClassInstanceFactory;
import io.github.shahbozolmosov.scanner.ClassScanner;
import io.github.shahbozolmosov.scanner.HandlerRegistrar;
import io.github.shahbozolmosov.scanner.resolver.*;
import io.github.shahbozolmosov.source.polling.PollingUpdateSource;
import io.github.shahbozolmosov.source.UpdateSource;
import io.github.shahbozolmosov.source.webhook.WebhookUpdateSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

public final class TelegramBot {

    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    private final String name;
    private final TelegramClient telegramClient;
    private final Registry registry;
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
        this.registry = new Registry();

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
        switch (config.getUpdatesMode()) {
            case POLLING:
                this.updateSource = new PollingUpdateSource(
                        telegramClient,
                        dispatcher,
                        executionMode,
                        config.getGlobalExceptionHandler()
                );
                log.info("Using POLLING mode");
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
                        // TODO: add webhook secret

                        config.getGlobalExceptionHandler()
                );
                log.info("Using WEBHOOK mode");
                break;
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

        String packageName = new ApplicationPackageResolver().resolve();

        handlerRegistrar.register(packageName);

        updateSource.start();

        Runtime.getRuntime().addShutdownHook(new Thread(this::stopBot));

        log.info("Started successfully");
    }

    public void stopBot() {
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
    }
}
