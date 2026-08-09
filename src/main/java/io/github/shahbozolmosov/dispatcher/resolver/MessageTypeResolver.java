package io.github.shahbozolmosov.dispatcher.resolver;

import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.type.MessageType;

import java.util.Optional;

public interface MessageTypeResolver {
    Optional<MessageType> resolve(Message message);
}
