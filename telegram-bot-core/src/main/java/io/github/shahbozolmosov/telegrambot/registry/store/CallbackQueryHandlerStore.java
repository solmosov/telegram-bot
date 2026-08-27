package io.github.shahbozolmosov.telegrambot.registry.store;

import io.github.shahbozolmosov.telegrambot.dispatcher.resolver.CallbackParamResolver;
import io.github.shahbozolmosov.telegrambot.exception.handler.HandlerRegistrationException;
import io.github.shahbozolmosov.telegrambot.handler.Handler;
import io.github.shahbozolmosov.telegrambot.registry.registration.CallbackQueryHandlerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CallbackQueryHandlerStore {

    private final String botName;
    private final Map<String, Handler> handlers = new HashMap<>();

    public CallbackQueryHandlerStore(
            String botName
    ) {
        this.botName = botName;
    }

    public void register(
            CallbackQueryHandlerRegistration registration
    ) {
        String mapKey = CallbackParamResolver.callbackKey(registration.key());

        Handler previous = handlers.putIfAbsent(mapKey, registration.handler());

        if (previous != null) {
            throw new HandlerRegistrationException(
                    "CallbackQueryHandler already registered for key='%s' in bot='%s'"
                            .formatted(
                                    registration.key().replace(botName, ""),
                                    botName
                            )
            );
        }

    }

    public List<Handler> find(String key) {
        String mapKey = CallbackParamResolver.updateKey(key);


        List<Handler> result = new ArrayList<>();

        Handler exactHandlers = handlers.get(key);

        if (exactHandlers != null) {
            result.add(exactHandlers);
        } else {
            Handler paramHandlers = handlers.get(mapKey);
            if (paramHandlers != null) {
                result.add(paramHandlers);
            }
        }


        if (key != null) {
            Handler globalHandlers = handlers.get(botName);

            if (globalHandlers != null) {
                result.add(globalHandlers);
            }
        }

        return result;
    }
}
