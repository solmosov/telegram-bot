package io.github.shahbozolmosov.registry.store;

import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.registration.UpdateHandlerRegistration;

import java.util.ArrayList;
import java.util.List;

public final class UpdateHandlerStore {
    private final List<Handler> updateHandlers = new ArrayList<>();

    public void register(UpdateHandlerRegistration registration) {
        updateHandlers.add(registration.handler());

    }

    public List<Handler> getHandlers() {
        return updateHandlers;
    }
}
