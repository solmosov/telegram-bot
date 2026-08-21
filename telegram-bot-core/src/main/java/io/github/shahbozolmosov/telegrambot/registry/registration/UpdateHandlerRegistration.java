package io.github.shahbozolmosov.telegrambot.registry.registration;

import io.github.shahbozolmosov.telegrambot.handler.Handler;

public record UpdateHandlerRegistration(
        String botName,
        Handler handler
) {
}
