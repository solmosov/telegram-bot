package io.github.shahbozolmosov.telegrambot.context;

import io.github.shahbozolmosov.telegrambot.model.From;
import io.github.shahbozolmosov.telegrambot.model.Message;

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

    public String messageId() {
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
