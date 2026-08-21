package io.github.shahbozolmosov.telegrambot.dispatcher.resolver;

import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.MessageType;

import java.util.Optional;

public final class RequestUsersMessageTypeResolver implements MessageTypeResolver {
    @Override
    public Optional<MessageType> resolve(Message message) {

        if (message.usersShared() != null) {
            return Optional.of(MessageType.USERS_SHARED);
        }

        return Optional.empty();
    }
}
