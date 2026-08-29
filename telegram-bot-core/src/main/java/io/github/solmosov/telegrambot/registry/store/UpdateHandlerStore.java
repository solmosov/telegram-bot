package io.github.solmosov.telegrambot.registry.store;

import io.github.solmosov.telegrambot.exception.handler.HandlerRegistrationException;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.registry.registration.UpdateHandlerRegistration;

import java.util.HashMap;
import java.util.Map;

public final class UpdateHandlerStore {

    private final String botName;
    private final Map<String, Handler> updateHandlers = new HashMap<>();


    public UpdateHandlerStore(
            String botName
    ) {
        this.botName = botName;
    }

    public void register(UpdateHandlerRegistration registration) {
        Handler previous = updateHandlers.putIfAbsent(registration.botName(), registration.handler());

        if (previous != null) {
            throw new HandlerRegistrationException(
                    "UpdateHandler already registered for bot='%s'".formatted(botName)
            );
        }
    }

    public Handler getHandler(String botName) {
        return updateHandlers.get(botName);
    }
}
