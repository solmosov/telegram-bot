package io.github.telegrambot.dispatcher.resolver;

import io.github.telegrambot.model.Message;
import io.github.telegrambot.model.MessageType;

public interface FallbackMessageTypeResolver {
    MessageType resolve(Message message);
}
