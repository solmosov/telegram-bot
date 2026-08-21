package io.github.telegrambot.registry.registration;

import io.github.telegrambot.handler.Handler;
import io.github.telegrambot.model.MessageType;

public record MessageHandlerRegistration(
        MessageType type,
        String key,
        Handler handler
) {
}
