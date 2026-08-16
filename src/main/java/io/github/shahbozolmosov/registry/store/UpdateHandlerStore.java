package io.github.shahbozolmosov.registry.store;

import io.github.shahbozolmosov.handler.Handler;

import java.util.ArrayList;
import java.util.List;

public final class UpdateHandlerStore {
    private final List<Handler> updateHandlers = new ArrayList<>();

    public void register(Handler handler) {
        updateHandlers.add(handler);

    }

    public List<Handler> getHandlers() {
        return updateHandlers;
    }
}
