package io.github.solmosov.telegrambot.messaging;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.messaging.builder.ChatActionBuilder;
import io.github.solmosov.telegrambot.messaging.builder.MessageBuilder;

public final class TelegramMessaging {


    private final TelegramClient client;

    public TelegramMessaging(TelegramClient client) {
        this.client = client;
    }

    public ChatActionBuilder chatAction(){
        return new ChatActionBuilder(
                client
        );
    }

    public MessageBuilder message(String text) {
        return new MessageBuilder(
                client,
                text
        );
    }

}
