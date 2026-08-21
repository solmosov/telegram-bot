package io.github.telegrambot.request.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.telegrambot.keyboard.ReplyMarkup;

public record EditMessageReplyMarkupRequest(
        @JsonProperty("chat_id")
        String chatId,

        @JsonProperty("message_id")
        long messageId,

        @JsonProperty("reply_markup")
        ReplyMarkup replyMarkup
) {
}
