package io.github.shahbozolmosov.registry.dto;

import io.github.shahbozolmosov.handler.Handler;

public record CallbackHandlerGroup(
        String callbackPattern,
        Handler handler
) {
}
