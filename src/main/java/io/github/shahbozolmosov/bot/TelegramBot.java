package io.github.shahbozolmosov.bot;

import io.github.shahbozolmosov.authorization.AuthorizationManager;
import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.dispatcher.CallbackQueryUpdateDispatcher;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.dispatcher.MessageUpdateDispatcher;
import io.github.shahbozolmosov.dispatcher.UpdateTypeDispatcher;
import io.github.shahbozolmosov.dispatcher.resolver.*;
import io.github.shahbozolmosov.exception.GlobalExceptionHandler;
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
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

public final class TelegramBot {

    private final TelegramClient telegramClient;
    private final Registry registry;
    private final Dispatcher dispatcher;
    private final HandlerRegistrar handlerRegistrar;
    private final JsonMapper jsonMapper;
    private UpdateSource updateSource;


    private final ExecutionMode executionMode;

    public TelegramBot(String botToken) {
        this(botToken, TelegramBotConfig.defaults());
    }

    public TelegramBot(String botToken, TelegramBotConfig config) {
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

        this.dispatcher = new Dispatcher(registry, updateTypeDispatchers, authorizationManager);
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
                System.out.println("[Telegram Bot] Using POLLING mode");
                break;
            case WEBHOOK:
                this.updateSource = new WebhookUpdateSource(
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
        }
    }

    public void start() {
        String packageName = new ApplicationPackageResolver().resolve();

        handlerRegistrar.register(packageName);

        updateSource.start();

        Runtime.getRuntime().addShutdownHook(new Thread(this::stopBot));

        System.out.println("[Telegram Bot] Started successfully");
    }

    public void stopBot() {
        System.out.println("[Telegram Bot] Shutdown signal received");

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

        System.out.println("[Telegram Bot] Bot stopped");
    }
}
