package io.github.shahbozolmosov.registry.registration;

import io.github.shahbozolmosov.handler.Handler;

public record UpdateHandlerRegistration(
        String botName,
        Handler handler
) {
}
