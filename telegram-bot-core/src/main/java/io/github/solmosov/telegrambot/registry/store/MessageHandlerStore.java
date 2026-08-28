package io.github.solmosov.telegrambot.registry.store;

import io.github.solmosov.telegrambot.exception.handler.HandlerRegistrationException;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.model.MessageType;
import io.github.solmosov.telegrambot.registry.registration.MessageHandlerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MessageHandlerStore {

    private final String botName;
    private final Map<MessageType, Map<String, Handler>> handlers = new HashMap<>();

    public MessageHandlerStore(
            String botName
    ) {
        this.botName = botName;
    }

    public void register(MessageHandlerRegistration registration) {
        Map<String, Handler> typedHandlers = handlers
                .computeIfAbsent(registration.type(), k -> new HashMap<>());

        Handler existingHandler = typedHandlers
                .putIfAbsent(registration.key(), registration.handler());

        if(existingHandler != null){
            throw new HandlerRegistrationException(
                    "MessageHandler already registered for type='%s' and key='%s' in bot='%s'"
                            .formatted(
                                    registration.type(),
                                    registration.key().replace(botName, ""),
                                    botName
                            )
            );
        }
    }

    public List<Handler> find(
            MessageType type,
            String key
    ) {
        Map<String, Handler> typeHandlers = handlers.get(type);

        if (typeHandlers == null) {
            return List.of();
        }

        List<Handler> result = new ArrayList<>();

        Handler exactHandlers = typeHandlers.get(key);

        if (exactHandlers != null) {
            result.add(exactHandlers);
        }

        if (key != null) {
            Handler globalHandler = typeHandlers.get(botName);

            if (globalHandler != null) {
                result.add(globalHandler);
            }
        }

        return result;
    }
}
