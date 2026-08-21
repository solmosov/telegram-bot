package io.github.shahbozolmosov.telegrambot.registry.store;

import io.github.shahbozolmosov.telegrambot.handler.Handler;
import io.github.shahbozolmosov.telegrambot.model.MessageType;
import io.github.shahbozolmosov.telegrambot.registry.registration.MessageHandlerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MessageHandlerStore {

    private final Map<MessageType, Map<String, List<Handler>>> handlers = new HashMap<>();

    public void register(MessageHandlerRegistration registration){
        handlers
                .computeIfAbsent(registration.type(), k -> new HashMap<>())
                .computeIfAbsent(registration.key(), k -> new ArrayList<>())
                .add(registration.handler());

    }

    public List<Handler> find(
            MessageType type,
            String key
    ) {
        Map<String, List<Handler>> typeHandlers = handlers.get(type);

        if (typeHandlers == null) {
            return List.of();
        }

        List<Handler> result = new ArrayList<>();

        List<Handler> exactHandlers = typeHandlers.get(key);

        if (exactHandlers != null) {
            result.addAll(exactHandlers);
        }

        if (key != null) {
            List<Handler> globalHandlers = typeHandlers.get(null);

            if (globalHandlers != null) {
                result.addAll(globalHandlers);
            }
        }

        return result;
    }
}
