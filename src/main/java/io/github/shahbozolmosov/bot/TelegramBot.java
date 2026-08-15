package io.github.shahbozolmosov.bot;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.dispatcher.CallbackQueryUpdateDispatcher;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.dispatcher.MessageUpdateDispatcher;
import io.github.shahbozolmosov.dispatcher.UpdateTypeDispatcher;
import io.github.shahbozolmosov.dispatcher.resolver.*;
import io.github.shahbozolmosov.polling.Polling;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.scanner.ApplicationPackageResolver;
import io.github.shahbozolmosov.scanner.ClassInstanceFactory;
import io.github.shahbozolmosov.scanner.ClassScanner;
import io.github.shahbozolmosov.scanner.HandlerRegistrar;
import io.github.shahbozolmosov.scanner.resolver.*;

import java.util.List;

public final class TelegramBot {

    private final TelegramClient telegramClient;
    private final Registry registry;
    private final Dispatcher dispatcher;
    private final HandlerRegistrar handlerRegistrar;
    private Polling polling;
    private Thread pollingThread;


    public TelegramBot(String botToken) {
        this.telegramClient = new TelegramClient(botToken);
        this.registry = new Registry();


        List<MessageTypeResolver> messageTypeResolvers = List.of(
                new CommandMessageTypeResolver(),
                new PhotoMessageTypeResolver(),
                new LocationMessageTypeResolver(),
                new ContactMessageTypeResolver(),
                new RequestUsersMessageTypeResolver()
        );
        FallbackMessageTypeResolver fallbackMessageTypeResolver = new TextMessageTypeResolver();

        List<UpdateTypeDispatcher> updateTypeDispatchers = List.of(
                new MessageUpdateDispatcher(registry, messageTypeResolvers, fallbackMessageTypeResolver),
                new CallbackQueryUpdateDispatcher(registry)
        );

        this.dispatcher = new Dispatcher(registry, updateTypeDispatchers);

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
    }

    public void start() {
        String packageName = new ApplicationPackageResolver().resolve();

        handlerRegistrar.register(packageName);

        // Polling
        Polling polling = new Polling(
                telegramClient,
                dispatcher
        );

        pollingThread = new Thread(() -> {
            try {
                polling.start();
            } finally {
                polling.shutdown();
            }
        });

        pollingThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(this::stopBot));

        System.out.println("[Telegram Bot] Started successfully");
    }

    public void stopBot() {
        System.out.println("[Telegram Bot] Shutdown signal received");

        if (polling != null) {
            polling.stop();
        }

        if (pollingThread != null) {
            try {
                pollingThread.interrupt();

                pollingThread.join(15_000);

                if (pollingThread.isAlive()) {
                    System.err.println("[Telegram Bot] Polling thread did not terminate gracefully");
                } else {
                    System.out.println("[Telegram Bot] Polling thread terminated successfully");
                }
            } catch (InterruptedException ex) {
                pollingThread.interrupt();
                Thread.currentThread().interrupt();
            }
        }

        telegramClient.shutdown();

        System.out.println("[Telegram Bot] Bot stopped");
    }
}
