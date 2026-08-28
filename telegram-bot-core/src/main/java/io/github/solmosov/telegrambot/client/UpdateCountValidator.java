package io.github.solmosov.telegrambot.client;

import io.github.solmosov.telegrambot.exception.client.TelegramClientException;
import io.github.solmosov.telegrambot.model.Update;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

public final class UpdateCountValidator {
    private final int maxUpdates;

    public UpdateCountValidator(
            int maxUpdates
    ) {
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
