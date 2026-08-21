package io.github.shahbozolmosov.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Update(
        @JsonProperty("update_id")
        long updateId,

        Message message,

        @JsonProperty("callback_query")
        CallbackQuery callbackQuery
) {
    public UpdateType type() {
        if (callbackQuery != null) {
            return UpdateType.CALLBACK_QUERY;
        }

        if (message != null) {
            return UpdateType.MESSAGE;
        }

        throw new IllegalArgumentException("Unknown update type");
    }
}
