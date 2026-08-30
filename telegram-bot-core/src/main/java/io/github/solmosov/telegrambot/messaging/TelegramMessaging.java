package io.github.solmosov.telegrambot.messaging;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.context.builder.MessageBuilder;

public final class TelegramMessaging {


    private final TelegramClient client;

    public TelegramMessaging(TelegramClient client) {
        this.client = client;
    }

    public MessageBuilder message(String text) {
        return new MessageBuilder(
                client,
                text
        );
    }

}
