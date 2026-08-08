package io.github.shahbozolmosov.bot;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.handler.CommandHandler;
import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.model.Update;

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

                Message message = update.message();

                if (message != null) {

                    CommandHandler handler = commandHandlers.get(message.text());
                    if (handler != null) {
                        handler.handle(update);
                    }
                }


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

}
