package io.github.solmosov.telegrambot.registry;

import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.model.MessageType;
import io.github.solmosov.telegrambot.registry.registration.CallbackQueryHandlerRegistration;
import io.github.solmosov.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.solmosov.telegrambot.registry.registration.UpdateHandlerRegistration;
import io.github.solmosov.telegrambot.registry.store.CallbackQueryHandlerStore;
import io.github.solmosov.telegrambot.registry.store.MessageHandlerStore;
import io.github.solmosov.telegrambot.registry.store.UpdateHandlerStore;

import java.util.List;

public final class Registry {

    private final MessageHandlerStore messageRegistry;
    private final CallbackQueryHandlerStore callbackRegistry;
    private final UpdateHandlerStore updateRegistry;

    public Registry(String botName) {
        this.messageRegistry = new MessageHandlerStore(botName);
        this.callbackRegistry = new CallbackQueryHandlerStore(botName);
        this.updateRegistry = new UpdateHandlerStore(botName);
    }


    // Message
    public void register(MessageHandlerRegistration registration) {
        messageRegistry.register(registration);
    }

    public List<Handler> find(
            MessageType type,
            String key
    ) {
        return messageRegistry.find(type, key);
    }


    // Callback
    public void registerCallbackQuery(CallbackQueryHandlerRegistration registration) {
        callbackRegistry.register(registration);
    }

    public List<Handler> findCallbackQuery(String key) {
        return callbackRegistry.find(key);
    }

    // Update
    public void registerUpdateHandler(UpdateHandlerRegistration registration) {
        updateRegistry.register(registration);

    }

    public Handler getUpdateHandler(String botName) {
        return updateRegistry.getHandler(botName);
    }
}
