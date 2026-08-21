package io.github.shahbozolmosov.telegrambot.registry.store;

import io.github.shahbozolmosov.telegrambot.dispatcher.resolver.CallbackParamResolver;
import io.github.shahbozolmosov.telegrambot.handler.Handler;
import io.github.shahbozolmosov.telegrambot.registry.registration.CallbackHandlerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CallbackHandlerStore {
    private final Map<String, List<CallbackHandlerGroup>> handlers = new HashMap<>();

    public void register(
            CallbackHandlerRegistration registration
    ) {
        String mapKey = CallbackParamResolver.callbackKey(registration.key());
        handlers.computeIfAbsent(mapKey, k -> new ArrayList<>())
                .add(new CallbackHandlerGroup(registration.key(), registration.handler()));
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


    public static record CallbackHandlerGroup(
            String callbackPattern,
            Handler handler
    ) {
    }
}
