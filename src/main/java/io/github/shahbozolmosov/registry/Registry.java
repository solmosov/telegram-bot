package io.github.shahbozolmosov.registry;

import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.model.MessageType;
import io.github.shahbozolmosov.registry.registration.MessageHandlerRegistration;
import io.github.shahbozolmosov.registry.store.CallbackHandlerStore;
import io.github.shahbozolmosov.registry.store.MessageHandlerStore;
import io.github.shahbozolmosov.registry.store.UpdateHandlerStore;

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
    public void registerCallbackQuery(String key, Handler handler) {
        callbackRegistry.register(key, handler);
    }

    public List<CallbackHandlerStore.CallbackHandlerGroup> findCallbackQuery(String key) {
        return callbackRegistry.find(key);
    }

    // Update
    public void registerUpdateHandler(Handler handler) {
        updateRegistry.register(handler);

    }

    public List<Handler> getUpdateHandlers() {
        return updateRegistry.getHandlers();
    }
}
