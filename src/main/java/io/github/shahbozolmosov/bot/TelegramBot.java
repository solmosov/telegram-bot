package io.github.shahbozolmosov.bot;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.dispatcher.CallbackQueryUpdateDispatcher;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.dispatcher.MessageUpdateDispatcher;
import io.github.shahbozolmosov.dispatcher.UpdateTypeDispatcher;
import io.github.shahbozolmosov.dispatcher.resolver.*;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.model.Update;
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


    public TelegramBot(String botToken) {
        this.telegramClient = new TelegramClient(botToken);
        this.registry = new Registry();


        List<MessageTypeResolver> messageTypeResolvers = List.of(
                new CommandMessageTypeResolver(),
                new PhotoMessageTypeResolver(),
                new LocationMessageTypeResolver()
        );
        FallbackMessageTypeResolver fallbackMessageTypeResolver = new TextMessageTypeResolver();

        List<UpdateTypeDispatcher> updateTypeDispatchers = List.of(
                new MessageUpdateDispatcher(registry, messageTypeResolvers, fallbackMessageTypeResolver),
                new CallbackQueryUpdateDispatcher(registry)
        );

        this.dispatcher = new Dispatcher(registry, updateTypeDispatchers);

        List<AnnotationHandlerResolver> annotationHandlerResolvers = List.of(
                new CommandAnnotationHandlerResolver(),
                new MessageAnnotationHandlerResolver(),
                new PhotoAnnotationHandlerResolver(),
                new CallbackAnnotationHandlerResolver(),
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
        polling.start();
    }
}
