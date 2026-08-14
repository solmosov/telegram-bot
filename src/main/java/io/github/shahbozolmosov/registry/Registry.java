package io.github.shahbozolmosov.registry;

import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.dto.CallbackHandlerGroup;
import io.github.shahbozolmosov.model.MessageType;

import java.util.List;

public final class Registry {

    private final MessageRegistry messageRegistry;
    private final CallbackRegistry callbackRegistry;
    private final UpdateRegistry updateRegistry;

    public Registry() {
        this.messageRegistry = new MessageRegistry();
        this.callbackRegistry = new CallbackRegistry();
        this.updateRegistry = new UpdateRegistry();
    }


    // Message
    public void register(HandlerMapping registration) {
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

    public List<CallbackHandlerGroup> findCallbackQuery(String key) {
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
