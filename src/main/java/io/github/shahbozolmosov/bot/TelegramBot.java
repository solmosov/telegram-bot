package io.github.shahbozolmosov.bot;

import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.registery.Registry;
import io.github.shahbozolmosov.scanner.ClassInstanceFactory;
import io.github.shahbozolmosov.scanner.ClassScanner;
import io.github.shahbozolmosov.type.MessageType;

import java.lang.reflect.Method;
import java.util.List;

public final class TelegramBot {

    private final TelegramClient telegramClient;
    private final Registry registry;
    private final Dispatcher dispatcher;

    public TelegramBot(String botToken) {
        this.telegramClient = new TelegramClient(botToken);
        this.registry = new Registry();
        this.dispatcher = new Dispatcher(registry);
    }

    public void start() {
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
    public void registerCommand(
            String command,
            Handler handler
    ) {
        registry.register(
                MessageType.COMMAND,
                command,
                handler
        );
    }

    // TODO: move to other class
    public void registerCommands() {
        String packageName = resolveApplicationPackage();
        ClassScanner scanner = new ClassScanner();

        List<Class<?>> classes = scanner.scan(packageName);
        ClassInstanceFactory factory = new ClassInstanceFactory();

        for (Class<?> clazz : classes) {

            Method[] methods = clazz.getDeclaredMethods();

            boolean hasCommand = false;

            for (Method method : methods) {
                if (method.isAnnotationPresent(Command.class)) {
                    hasCommand = true;
                    break;
                }
            }

            if (!hasCommand) {
                continue;
            }

            Object instance = factory.create(clazz);

            for (Method method : clazz.getDeclaredMethods()) {
                Command command = method.getAnnotation(Command.class);

                if (command == null) {
                    continue;
                }

                Handler handler = context -> {
                    try {
                        method.invoke(instance, context);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                };


                registerCommand(command.value(), handler);
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
