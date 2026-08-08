package io.github.shahbozolmosov.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Message(
        @JsonProperty("message_id")
        long messageId,
        Chat chat,
        String text
) {
}
