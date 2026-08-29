package io.github.solmosov.telegrambot.registry.store;

import io.github.solmosov.telegrambot.dispatcher.resolver.CallbackParamResolver;
import io.github.solmosov.telegrambot.exception.handler.HandlerRegistrationException;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.registry.registration.CallbackQueryHandlerRegistration;

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

        Handler exactHandler = handlers.get(key);

        if (exactHandler != null) {
            result.add(exactHandler);
        } else {
            Handler paramHandlers = handlers.get(mapKey);
            if (paramHandlers != null) {
                result.add(paramHandlers);
            }
        }


        Handler globalHandler = handlers.get(botName);

        if (globalHandler != null) {
            result.add(globalHandler);
        }

        return result;
    }
}
