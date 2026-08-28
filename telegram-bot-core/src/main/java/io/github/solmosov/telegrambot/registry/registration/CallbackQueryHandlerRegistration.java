package io.github.solmosov.telegrambot.registry.registration;

import io.github.solmosov.telegrambot.handler.Handler;

public record CallbackQueryHandlerRegistration(
        String key,
        Handler handler
) {
}
