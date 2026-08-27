package io.github.shahbozolmosov.telegrambot.model.message;

import io.github.shahbozolmosov.telegrambot.model.Chat;
import io.github.shahbozolmosov.telegrambot.model.From;

public abstract class AbstractMessage {
    private final long messageId;
    private final From from;
    private final Chat chat;
    private final long date;

    protected AbstractMessage(
            long messageId,
            From from,
            Chat chat,
            long date
    ) {
        this.messageId = messageId;
        this.from = from;
        this.chat = chat;
        this.date = date;
    }

    public long messageId() {
        return messageId;
    }

    public From from() {
        return from;
    }

    public Chat chat() {
        return chat;
    }

    public long date() {
        return date;
    }
}
