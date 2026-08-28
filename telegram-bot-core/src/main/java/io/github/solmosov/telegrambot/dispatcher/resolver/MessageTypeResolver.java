package io.github.solmosov.telegrambot.dispatcher.resolver;

import io.github.solmosov.telegrambot.model.Message;
import io.github.solmosov.telegrambot.model.MessageType;

import java.util.Optional;

public interface MessageTypeResolver {
    Optional<MessageType> resolve(Message message);
}
