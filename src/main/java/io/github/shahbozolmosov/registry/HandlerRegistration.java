package io.github.shahbozolmosov.registry;

import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.type.MessageType;

public record HandlerRegistration(
        MessageType type,
        String key,
        Handler handler
) {
}
