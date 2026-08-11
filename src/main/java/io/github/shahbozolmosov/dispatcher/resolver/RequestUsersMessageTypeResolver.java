package io.github.shahbozolmosov.dispatcher.resolver;

import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.type.MessageType;

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
