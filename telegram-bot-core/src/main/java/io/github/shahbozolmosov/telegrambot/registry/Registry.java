package io.github.shahbozolmosov.telegrambot.registry;

import io.github.shahbozolmosov.telegrambot.handler.Handler;
import io.github.shahbozolmosov.telegrambot.model.MessageType;
import io.github.shahbozolmosov.telegrambot.registry.registration.CallbackHandlerRegistration;
import io.github.shahbozolmosov.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.shahbozolmosov.telegrambot.registry.registration.UpdateHandlerRegistration;
import io.github.shahbozolmosov.telegrambot.registry.store.CallbackHandlerStore;
import io.github.shahbozolmosov.telegrambot.registry.store.MessageHandlerStore;
import io.github.shahbozolmosov.telegrambot.registry.store.UpdateHandlerStore;

import java.util.List;

public final class Registry {

    private final MessageHandlerStore messageRegistry;
    private final CallbackHandlerStore callbackRegistry;
    private final UpdateHandlerStore updateRegistry;

    public Registry(String botName) {
        this.messageRegistry = new MessageHandlerStore(botName);
        this.callbackRegistry = new CallbackHandlerStore(botName);
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
    public void registerCallbackQuery(CallbackHandlerRegistration registration) {
        callbackRegistry.register(registration);
    }

    public List<Handler> findCallbackQuery(String key) {
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
