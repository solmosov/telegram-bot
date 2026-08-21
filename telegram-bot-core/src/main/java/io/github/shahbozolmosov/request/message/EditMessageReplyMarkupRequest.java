package io.github.shahbozolmosov.request.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.keyboard.ReplyMarkup;

public record EditMessageReplyMarkupRequest(
        @JsonProperty("chat_id")
        String chatId,

        @JsonProperty("message_id")
        long messageId,

        @JsonProperty("reply_markup")
        ReplyMarkup replyMarkup
) {
}
