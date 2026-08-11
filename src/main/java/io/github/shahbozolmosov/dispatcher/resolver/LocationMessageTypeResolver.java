package io.github.shahbozolmosov.dispatcher.resolver;

import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.type.MessageType;

import java.util.Optional;

public class LocationMessageTypeResolver implements MessageTypeResolver {
    @Override
    public Optional<MessageType> resolve(Message message) {

        if (message.location() != null) {
            return Optional.of(MessageType.LOCATION);
        }

        return Optional.empty();
    }
}
