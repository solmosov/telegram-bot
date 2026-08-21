package io.github.shahbozolmosov.telegrambot.dispatcher.resolver;

import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.MessageType;

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
