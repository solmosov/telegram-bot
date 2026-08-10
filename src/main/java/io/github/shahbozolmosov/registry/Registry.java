package io.github.shahbozolmosov.registry;

import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.type.MessageType;

import java.util.List;

public final class Registry {

    private final MessageRegistry messageRegistry;
    private final UpdateRegistry updateRegistry;


    public Registry() {
        this.messageRegistry = new MessageRegistry();
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


    // Update
    public void registerUpdateHandler(Handler handler) {
        updateRegistry.register(handler);

    }
    public List<Handler> getUpdateHandlers() {
        return updateRegistry.getHandlers();
    }
}
