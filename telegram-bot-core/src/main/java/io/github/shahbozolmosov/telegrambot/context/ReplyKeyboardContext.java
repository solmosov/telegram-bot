package io.github.shahbozolmosov.telegrambot.context;

import io.github.shahbozolmosov.telegrambot.model.Contact;
import io.github.shahbozolmosov.telegrambot.model.Location;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.UsersShared;

import java.util.List;
import java.util.Optional;

public final class ReplyKeyboardContext {

    private final MessageContext messageContext;

    public ReplyKeyboardContext(
            MessageContext messageContext
    ) {
        this.messageContext = messageContext;
    }


    public Message message() {
        return messageContext.message();
    }

    public String text() {
        return messageContext.text();
    }

    public Optional<Location> location() {
        return Optional.ofNullable(messageContext.message().location());
    }

    public Contact contact() {
        return messageContext.message().contact();
    }

    public UsersShared usersShared() {
        return messageContext.message().usersShared();
    }

    public List<Long> usersSharedUserIds() {
        return usersShared().userIds();
    }

    public List<UsersShared.User> usersSharedUsers() {
        return usersShared().users();
    }

    public Integer usersSharedRequestId() {
        return usersShared().requestId();
    }
}
