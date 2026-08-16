package io.github.shahbozolmosov.bot;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.dispatcher.CallbackQueryUpdateDispatcher;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.dispatcher.MessageUpdateDispatcher;
import io.github.shahbozolmosov.dispatcher.UpdateTypeDispatcher;
import io.github.shahbozolmosov.dispatcher.resolver.*;
import io.github.shahbozolmosov.polling.SingleThreadUpdateExecutor;
import io.github.shahbozolmosov.polling.UpdateExecutor;
import io.github.shahbozolmosov.polling.VirtualThreadUpdateExecutor;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.scanner.ApplicationPackageResolver;
import io.github.shahbozolmosov.scanner.ClassInstanceFactory;
import io.github.shahbozolmosov.scanner.ClassScanner;
import io.github.shahbozolmosov.scanner.HandlerRegistrar;
import io.github.shahbozolmosov.scanner.resolver.*;
import io.github.shahbozolmosov.source.PollingUpdateSource;
import io.github.shahbozolmosov.source.UpdateSource;

import java.util.List;

public final class TelegramBot {

    private final TelegramClient telegramClient;
    private final Registry registry;
    private final Dispatcher dispatcher;
    private final HandlerRegistrar handlerRegistrar;
    private UpdateSource updateSource;


    private final ExecutionMode executionMode;

    public TelegramBot(String botToken) {
        this(botToken, TelegramBotConfig.defaults());
    }

    public TelegramBot(String botToken, TelegramBotConfig config) {
        this.executionMode = config.getExecutionMode();

        this.telegramClient = new TelegramClient(botToken);
        this.registry = new Registry();


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
                new MessageUpdateDispatcher(registry, messageTypeResolvers, fallbackMessageTypeResolver),
                new CallbackQueryUpdateDispatcher(registry)
        );

        this.dispatcher = new Dispatcher(registry, updateTypeDispatchers);

        // Annotation Resolvers
        List<AnnotationHandlerResolver> annotationHandlerResolvers = List.of(
                // Message
                new CommandAnnotationHandlerResolver(),
                new PhotoAnnotationHandlerResolver(),
                new LocationHandlerAnnotationHandlerResolver(),
                new ContactHandlerAnnotationHandlerResolver(),
                new RequestUsersHandlerAnnotationHandlerResolver(),

                new MessageAnnotationHandlerResolver(),

                // Callback
                new CallbackAnnotationHandlerResolver(),

                // Update
                new UpdateAnnotationHandlerResolver()
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
                        executionMode
                );
                System.out.println("[Telegram Bot] Using POLLING mode");
                break;
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
