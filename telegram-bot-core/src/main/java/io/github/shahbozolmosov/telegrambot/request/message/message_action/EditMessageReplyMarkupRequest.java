package io.github.shahbozolmosov.telegrambot.request.message.message_action;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;

public record EditMessageReplyMarkupRequest(
        @JsonProperty("chat_id")
        String chatId,

        @JsonProperty("message_id")
        long messageId,

        @JsonProperty("reply_markup")
        ReplyMarkup replyMarkup
) {
}
