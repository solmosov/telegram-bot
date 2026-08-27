package io.github.shahbozolmosov.telegrambot.model.message;

import io.github.shahbozolmosov.telegrambot.model.Chat;
import io.github.shahbozolmosov.telegrambot.model.From;

public final class TextMessage extends Message {

    private final String text;

    public TextMessage(
            long messageId,
            From from,
            Chat chat,
            long date,
            String text
    ) {
        super(messageId, from, chat, date);
        this.text = text;
    }

    public String text() {
        return text;
    }
}
