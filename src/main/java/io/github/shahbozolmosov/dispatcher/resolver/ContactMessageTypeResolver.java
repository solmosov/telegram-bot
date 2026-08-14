package io.github.shahbozolmosov.dispatcher.resolver;

import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.MessageType;

import java.util.Optional;

public class ContactMessageTypeResolver implements MessageTypeResolver {
    @Override
    public Optional<MessageType> resolve(Message message) {

        if (message.contact() != null) {
            return Optional.of(MessageType.CONTACT);
        }

        return Optional.empty();
    }
}
