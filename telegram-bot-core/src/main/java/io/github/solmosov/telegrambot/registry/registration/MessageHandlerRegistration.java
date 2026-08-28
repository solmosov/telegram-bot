package io.github.solmosov.telegrambot.registry.registration;

import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.model.MessageType;

public record MessageHandlerRegistration(
        MessageType type,
        String key,
        Handler handler
) {
}
