package io.github.solmosov.telegrambot.request.message.message_action;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;

public record EditMessageReplyMarkupRequest(
        @JsonProperty("chat_id")
        long chatId,

        @JsonProperty("message_id")
        long messageId,

        @JsonProperty("reply_markup")
        ReplyMarkup replyMarkup
) {
}
