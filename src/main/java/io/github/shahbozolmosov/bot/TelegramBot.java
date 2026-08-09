package io.github.shahbozolmosov.bot;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.scanner.ClassInstanceFactory;
import io.github.shahbozolmosov.scanner.ClassScanner;
import io.github.shahbozolmosov.scanner.HandlerRegistrar;

import java.util.List;

public final class TelegramBot {

    private final TelegramClient telegramClient;
    private final Registry registry;
    private final Dispatcher dispatcher;
    private final HandlerRegistrar handlerRegistrar;

    public TelegramBot(String botToken) {
        this.telegramClient = new TelegramClient(botToken);
        this.registry = new Registry();
        this.dispatcher = new Dispatcher(registry);
        this.handlerRegistrar = new HandlerRegistrar(
                new ClassScanner(),
                new ClassInstanceFactory(),
                registry
        );
    }

    public void start() {
        String packageName = resolveApplicationPackage();

        handlerRegistrar.register(packageName);

        long offset = 0;

        while (true) {
            TelegramResponse<List<Update>> res = telegramClient.getUpdates(offset);

            for (Update update : res.result()) {
                offset = update.updateId() + 1;

                BotContext context = new BotContext(
                        telegramClient,
                        update
                );

                dispatcher.dispatch(update, context);

                System.out.println("Processing update: " + update.updateId());
            }
        }
    }

    // TODO: move to other class
    private String resolveApplicationPackage() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (!element.getMethodName().equals("main")) {
                continue;
            }

            try {
                Class<?> mainClass = Class.forName(element.getClassName());

                return mainClass.getPackageName();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

        throw new IllegalArgumentException(
                "Main application class was not found"
        );
    }
}
