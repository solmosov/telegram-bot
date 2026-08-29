package io.github.solmosov.telegrambot.dispatcher.resolver;

import io.github.solmosov.telegrambot.model.Message;
import io.github.solmosov.telegrambot.model.MessageType;

public final class TextMessageTypeResolver implements FallbackMessageTypeResolver {

    @Override
    public MessageType resolve(Message message) {
        return MessageType.TEXT;
    }
}
