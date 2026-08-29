package io.github.solmosov.telegrambot.request.message.message_action;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeleteMessageRequest(
        @JsonProperty("chat_id")
        long chatId,

        @JsonProperty("message_id")
        long messageId
) {
}
