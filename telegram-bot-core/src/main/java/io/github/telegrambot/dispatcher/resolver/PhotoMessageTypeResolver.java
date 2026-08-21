package io.github.telegrambot.dispatcher.resolver;

import io.github.telegrambot.model.Message;
import io.github.telegrambot.model.MessageType;

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
