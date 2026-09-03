package io.github.solmosov.telegrambot.context;

import io.github.solmosov.telegrambot.model.*;

import java.util.List;
import java.util.Locale;

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

    public Long date() {
        return message.date();
    }

    public String text() {
        return message.text();
    }

    public List<PhotoSize> photo() {
        return message.photo();
    }

    public DocumentInfo document() {
        return message.document();
    }

    public String caption() {
        return message.caption();
    }

    public ReplyToMessage replyToMessage() {
        return message.replyToMessage();
    }

    public Location location() {
        return message.location();
    }

    public Contact contact() {
        return message.contact();
    }

    public UsersShared usersShared() {
        return message.usersShared();
    }
}
