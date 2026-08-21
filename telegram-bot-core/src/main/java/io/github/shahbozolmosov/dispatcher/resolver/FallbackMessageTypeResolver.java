package io.github.shahbozolmosov.dispatcher.resolver;

import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.MessageType;

public interface FallbackMessageTypeResolver {
    MessageType resolve(Message message);
}
