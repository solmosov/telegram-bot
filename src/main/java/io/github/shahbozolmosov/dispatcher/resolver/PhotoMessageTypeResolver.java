package io.github.shahbozolmosov.dispatcher.resolver;

import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.type.MessageType;

import java.util.Optional;

public class PhotoMessageTypeResolver implements MessageTypeResolver {
    @Override
    public Optional<MessageType> resolve(Message message) {
        if (message.photo() != null && !message.photo().isEmpty()) {
            return Optional.of(MessageType.PHOTO);
        }

        return Optional.empty();
    }
}
