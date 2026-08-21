package io.github.shahbozolmosov.dispatcher.resolver;

import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.MessageType;

import java.util.Optional;

public final class CommandMessageTypeResolver implements MessageTypeResolver {
    @Override
    public Optional<MessageType> resolve(Message message) {
        String text = message.text();

        if (text != null && text.startsWith("/")) {
            return Optional.of(MessageType.COMMAND);
        }

        return Optional.empty();
    }
}
