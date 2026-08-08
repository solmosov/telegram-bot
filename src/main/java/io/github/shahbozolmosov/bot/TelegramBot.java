package io.github.shahbozolmosov.bot;

import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.handler.CommandHandler;
import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.model.Update;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TelegramBot {

    private final TelegramClient telegramClient;
    private final Map<String, CommandHandler> commandHandlers;

    public TelegramBot(String botToken) {
        this.telegramClient = new TelegramClient(botToken);
        this.commandHandlers = new HashMap<>();
    }

    public void start() {
        long offset = 0;

        while (true) {
            TelegramResponse<List<Update>> res = telegramClient.getUpdates(offset);

            for (Update update : res.result()) {
                offset = update.updateId() + 1;

                processUpdate(update);

                System.out.println("Processing update: " + update.updateId());
            }
        }
    }

    public void registerCommand(
            String command,
            CommandHandler handler
    ) {
        commandHandlers.put(command, handler);
    }

    public void registerCommands(Object instance) {
        Class<?> clazz = instance.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            Command command = method.getAnnotation(Command.class);


            if (command == null) {
                continue;
            }

            CommandHandler handler = context -> {
                try {
                    method.invoke(instance, context);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            };


            registerCommand(command.value(), handler);
        }
    }

    private void processUpdate(Update update) {
        Message message = update.message();

        if (message != null) {
            CommandHandler handler = commandHandlers.get(message.text());

            if (handler != null) {
                BotContext context = new BotContext(
                        telegramClient,
                        update
                );
                handler.handle(context);
            }
        }

        System.out.println("Processing update: " + update.updateId());
    }

}
