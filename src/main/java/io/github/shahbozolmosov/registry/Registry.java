package io.github.shahbozolmosov.registry;

import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.type.MessageType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Registry {
    private final Map<MessageType, Map<String, List<Handler>>> handlers = new HashMap<>();
    private final List<Handler> updateHandlers = new ArrayList<>();

    public void register(HandlerMapping registration) {
        handlers
                .computeIfAbsent(registration.type(), k -> new HashMap<>())
                .computeIfAbsent(registration.key(), k -> new ArrayList<>())
                .add(registration.handler());
    }

    public void registerUpdateHandler(Handler handler) {
        updateHandlers.add(handler);
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

        List<Handler> globalHandlers = typeHandlers.get(null);

        if (globalHandlers != null) {
            result.addAll(globalHandlers);
        }

        return result;
    }

    public  List<Handler> getUpdateHandlers(){
        return updateHandlers;
    }
}
