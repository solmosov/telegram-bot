package io.github.telegrambot.registry;

import io.github.telegrambot.handler.Handler;
import io.github.telegrambot.model.MessageType;
import io.github.telegrambot.registry.registration.CallbackHandlerRegistration;
import io.github.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.telegrambot.registry.registration.UpdateHandlerRegistration;
import io.github.telegrambot.registry.store.CallbackHandlerStore;
import io.github.telegrambot.registry.store.MessageHandlerStore;
import io.github.telegrambot.registry.store.UpdateHandlerStore;

import java.util.List;

public final class Registry {

    private final MessageHandlerStore messageRegistry;
    private final CallbackHandlerStore callbackRegistry;
    private final UpdateHandlerStore updateRegistry;

    public Registry() {
        this.messageRegistry = new MessageHandlerStore();
        this.callbackRegistry = new CallbackHandlerStore();
        this.updateRegistry = new UpdateHandlerStore();
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
    public void registerCallbackQuery(CallbackHandlerRegistration registration) {
        callbackRegistry.register(registration);
    }

    public List<CallbackHandlerStore.CallbackHandlerGroup> findCallbackQuery(String key) {
        return callbackRegistry.find(key);
    }

    // Update
    public void registerUpdateHandler(UpdateHandlerRegistration registration) {
        updateRegistry.register(registration);

    }

    public List<Handler> getUpdateHandlers(String botName) {
        return updateRegistry.getHandlers(botName);
    }
}
