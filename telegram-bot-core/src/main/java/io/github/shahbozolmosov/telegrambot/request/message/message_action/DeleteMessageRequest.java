package io.github.shahbozolmosov.telegrambot.request.message.message_action;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeleteMessageRequest(
        @JsonProperty("chat_id")
        String chatId,

        @JsonProperty("message_id")
        String messageId
) {
}
