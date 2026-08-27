package io.github.shahbozolmosov.telegrambot.registry.registration;

import io.github.shahbozolmosov.telegrambot.handler.Handler;

public record CallbackQueryHandlerRegistration(
        String key,
        Handler handler
) {
}
