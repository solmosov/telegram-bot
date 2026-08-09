package io.github.shahbozolmosov.context;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.model.From;
import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.Update;

public final class BotContext {

    private final TelegramClient telegramClient;
    private final Update update;

    public BotContext(
            TelegramClient client,
            Update update
    ) {
        this.telegramClient = client;
        this.update = update;
    }

    public Update update() {
        return update;
    }

    public Message message() {
        return update.message();
    }

    public long chatId() {
        return update.message().chat().id();
    }

    public From from() {
        return update.message().from();
    }

    public String text() {
        return update.message().text();
    }

    public void sendMessage(String text) {
        System.out.println("[BotContext] sendMessage: " + update.message().chat().id() + " = " + text);
        telegramClient.sendMessage(
                update.message().chat().id(),
                text
        );
    }
}
