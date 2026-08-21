package io.github.shahbozolmosov.client;

import io.github.shahbozolmosov.exception.client.TelegramClientException;
import io.github.shahbozolmosov.model.Update;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

public final class UpdateCountValidator {
    private final int maxUpdates;
    private final ObjectMapper objectMapper;

    public UpdateCountValidator(
            ObjectMapper objectMapper,
            int maxUpdates
    ) {
        this.objectMapper = objectMapper;
        this.maxUpdates = maxUpdates;
    }

    public void validate(List<Update> updates) {
        if (updates != null && updates.size() > maxUpdates) {
            throw new TelegramClientException(
                    "Too many updates received: " + updates.size() + " (max: " + maxUpdates + ")"
            );
        }
    }
}
