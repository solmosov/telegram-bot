package io.github.shahbozolmosov.telegrambot.registry.registration;

import io.github.shahbozolmosov.telegrambot.handler.Handler;
import io.github.shahbozolmosov.telegrambot.model.MessageType;

public record MessageHandlerRegistration(
        MessageType type,
        String key,
        Handler handler
) {
}
