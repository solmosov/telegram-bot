package io.github.shahbozolmosov.registery;

import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.type.MessageType;

import java.util.HashMap;
import java.util.Map;

public final class Registry {
    private final Map<MessageType, Map<String, Handler>> handlers = new HashMap<>();

    public void register(
            MessageType type,
            String key,
            Handler handler
    ) {
        handlers
                .computeIfAbsent(type, k -> new HashMap<>())
                .put(key, handler);
    }

    public Handler find(
            MessageType type,
            String key
    ) {
        Map<String, Handler> typeHandlers = handlers.get(type);

        if (typeHandlers == null) {
            return null;
        }

        return typeHandlers.get(key);
    }
}
