package io.github.shahbozolmosov.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Message(
        @JsonProperty("message_id")
        long messageId,
        From from,
        Chat chat,
        String text,
        List<PhotoSize> photo
) {
}
