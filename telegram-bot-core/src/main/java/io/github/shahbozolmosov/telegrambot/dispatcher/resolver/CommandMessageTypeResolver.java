package io.github.shahbozolmosov.telegrambot.dispatcher.resolver;

import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.MessageType;

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
