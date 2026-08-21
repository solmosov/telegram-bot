package io.github.shahbozolmosov.telegrambot.dispatcher.resolver;

import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.MessageType;

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
