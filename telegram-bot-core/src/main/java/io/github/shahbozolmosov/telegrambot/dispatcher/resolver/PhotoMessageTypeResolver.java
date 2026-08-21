package io.github.shahbozolmosov.telegrambot.dispatcher.resolver;

import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.MessageType;

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
