package io.github.shahbozolmosov.registry;

import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.type.MessageType;

import java.util.HashMap;
import java.util.Map;

public final class Registry {
    private final Map<MessageType, Map<String, Handler>> handlers = new HashMap<>();

    public void register(HandlerRegistration registration) {
        handlers
                .computeIfAbsent(registration.type(), k -> new HashMap<>())
                .put(registration.key(), registration.handler());
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
