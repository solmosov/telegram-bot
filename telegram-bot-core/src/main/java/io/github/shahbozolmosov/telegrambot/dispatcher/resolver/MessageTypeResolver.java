package io.github.shahbozolmosov.telegrambot.dispatcher.resolver;

import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.MessageType;

import java.util.Optional;

public interface MessageTypeResolver {
    Optional<MessageType> resolve(Message message);
}
