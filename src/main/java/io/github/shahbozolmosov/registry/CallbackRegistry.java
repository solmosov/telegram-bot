package io.github.shahbozolmosov.registry;

import io.github.shahbozolmosov.dispatcher.resolver.CallbackParamResolver;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.dto.CallbackHandlerGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CallbackRegistry {
    private final Map<String, List<CallbackHandlerGroup>> handlers = new HashMap<>();

    public void register(
            String key,
            Handler handler
    ) {
        String mapKey = CallbackParamResolver.callbackKey(key);
        handlers.computeIfAbsent(mapKey, k -> new ArrayList<>())
                .add(new CallbackHandlerGroup(key, handler));
    }

    public List<CallbackHandlerGroup> find(String key) {
        String mapKey = CallbackParamResolver.updateKey(key);


        List<CallbackHandlerGroup> result = new ArrayList<>();

        List<CallbackHandlerGroup> exactHandlers = handlers.get(key);

        if (exactHandlers != null) {
            result.addAll(exactHandlers);
        } else {
            List<CallbackHandlerGroup> paramHandlers = handlers.get(mapKey);
            if (paramHandlers != null) {
                result.addAll(paramHandlers);
            }
        }


        if (key != null) {
            List<CallbackHandlerGroup> globalHandlers = handlers.get(null);

            if (globalHandlers != null) {
                result.addAll(globalHandlers);
            }
        }

        return result;
    }
}
