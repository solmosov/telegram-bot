package io.github.shahbozolmosov.registry.registration;

import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.model.MessageType;

public record HandlerMapping(
        MessageType type,
        String key,
        Handler handler
) {
}
