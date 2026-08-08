package io.github.shahbozolmosov.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Update(
        @JsonProperty("update_id")
        long updateId,

        Message message
) {
}
