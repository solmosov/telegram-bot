package io.github.telegrambot.registry.registration;

import io.github.telegrambot.handler.Handler;

public record UpdateHandlerRegistration(
        String botName,
        Handler handler
) {
}
