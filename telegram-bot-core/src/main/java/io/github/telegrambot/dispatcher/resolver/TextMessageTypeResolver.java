package io.github.telegrambot.dispatcher.resolver;

import io.github.telegrambot.model.Message;
import io.github.telegrambot.model.MessageType;

public final class TextMessageTypeResolver implements FallbackMessageTypeResolver {

    @Override
    public MessageType resolve(Message message) {
        return MessageType.TEXT;
    }
}
