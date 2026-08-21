package io.github.shahbozolmosov.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReplyToMessage(
        @JsonProperty("message_id")
        String messageId,
        From from,
        Chat chat,
        String text
) {
}
