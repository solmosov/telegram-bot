package io.github.telegrambot.registry.registration;

import io.github.telegrambot.handler.Handler;

public record CallbackHandlerRegistration(
        String key,
        Handler handler
) {
}
