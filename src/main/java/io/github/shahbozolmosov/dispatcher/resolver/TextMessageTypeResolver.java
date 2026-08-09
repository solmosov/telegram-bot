package io.github.shahbozolmosov.dispatcher.resolver;

import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.type.MessageType;

public final class TextMessageTypeResolver implements FallbackMessageTypeResolver {

    @Override
    public MessageType resolve(Message message) {
        return MessageType.TEXT;
    }
}
