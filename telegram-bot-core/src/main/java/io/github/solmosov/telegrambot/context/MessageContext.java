package io.github.solmosov.telegrambot.context;

import io.github.solmosov.telegrambot.model.From;
import io.github.solmosov.telegrambot.model.Message;

public final class MessageContext {
    private final Message message;

    public MessageContext(
            Message message
    ) {
        this.message = message;
    }

    public Message message() {
        return message;
    }

    public long messageId() {
        return message().messageId();
    }

    public Long chatId() {
        return message.chat().id();
    }

    public From from() {
        return message.from();
    }

    public String text() {
        return message.text();
    }
}
