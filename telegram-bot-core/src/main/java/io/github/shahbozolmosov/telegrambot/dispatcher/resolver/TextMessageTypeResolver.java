package io.github.shahbozolmosov.telegrambot.dispatcher.resolver;

import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.MessageType;

public final class TextMessageTypeResolver implements FallbackMessageTypeResolver {

    @Override
    public MessageType resolve(Message message) {
        return MessageType.TEXT;
    }
}
