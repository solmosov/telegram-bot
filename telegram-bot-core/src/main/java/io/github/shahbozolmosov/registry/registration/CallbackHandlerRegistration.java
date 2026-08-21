package io.github.shahbozolmosov.registry.registration;

import io.github.shahbozolmosov.handler.Handler;

public record CallbackHandlerRegistration(
        String key,
        Handler handler
) {
}
