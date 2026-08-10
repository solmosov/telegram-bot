package io.github.shahbozolmosov.registry;

import io.github.shahbozolmosov.handler.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CallbackRegistry {
    private final Map<String, List<Handler>> handlers = new HashMap<>();

    public void register(
            String key,
            Handler handler
    ) {
        handlers.computeIfAbsent(key, k -> new ArrayList<>())
                .add(handler);
    }

    public List<Handler> find(String key) {
        List<Handler> result = new ArrayList<>();

        List<Handler> exactHandlers = handlers.get(key);

        if (exactHandlers != null) {
            result.addAll(exactHandlers);
        }

        if (key != null) {
            List<Handler> globalHandlers = handlers.get(null);

            if (globalHandlers != null) {
                result.addAll(globalHandlers);
            }
        }

        return result;
    }
}
