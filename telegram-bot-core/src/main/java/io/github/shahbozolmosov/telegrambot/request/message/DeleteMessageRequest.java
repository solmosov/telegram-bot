package io.github.shahbozolmosov.telegrambot.request.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeleteMessageRequest(
        @JsonProperty("chat_id")
        String chatId,

        @JsonProperty("message_id")
        long messageId
) {
}
