package io.github.shahbozolmosov.telegrambot.registry.store;

import io.github.shahbozolmosov.telegrambot.handler.Handler;
import io.github.shahbozolmosov.telegrambot.registry.registration.UpdateHandlerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UpdateHandlerStore {

    private final String botName;
    private final Map<String, List<Handler>> updateHandlers = new HashMap<>();


    public UpdateHandlerStore(
            String botName
    ) {
        this.botName = botName;
    }

    public void register(UpdateHandlerRegistration registration) {
        updateHandlers
                .computeIfAbsent(registration.botName(), k -> new ArrayList<>())
                .add(registration.handler());

    }

    public List<Handler> getHandlers(String botName) {
        List<Handler> list = updateHandlers.get(botName);

        if (list == null) {
            return List.of();
        }

        return list;
    }
}
