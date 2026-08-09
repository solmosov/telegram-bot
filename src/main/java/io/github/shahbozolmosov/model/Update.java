package io.github.shahbozolmosov.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.type.UpdateType;

public record Update(
        @JsonProperty("update_id")
        long updateId,

        Message message
) {
    public UpdateType type() {
        return UpdateType.MESSAGE;
    }
}
