package io.github.telegrambot.request.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.telegrambot.keyboard.ReplyMarkup;
import io.github.telegrambot.model.ParseMode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EditMessageRequest(
        @JsonProperty("chat_id")
        String chatId,

        @JsonProperty("message_id")
        long messageId,

        String text,

        @JsonProperty("parse_mode")
        ParseMode parseMode,

        @JsonProperty("reply_markup")
        ReplyMarkup replyMarkup
) {
    public EditMessageRequest(
            String chatId,
            long messageId,
            String text,
            ReplyMarkup replyMarkup
    ) {
        this(chatId, messageId, text, null, replyMarkup);
    }
}
