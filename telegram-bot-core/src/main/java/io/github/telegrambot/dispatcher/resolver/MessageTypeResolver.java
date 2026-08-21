package io.github.telegrambot.dispatcher.resolver;

import io.github.telegrambot.model.Message;
import io.github.telegrambot.model.MessageType;

import java.util.Optional;

public interface MessageTypeResolver {
    Optional<MessageType> resolve(Message message);
}
