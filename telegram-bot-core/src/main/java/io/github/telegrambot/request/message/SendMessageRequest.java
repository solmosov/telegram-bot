package io.github.telegrambot.request.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.telegrambot.keyboard.ReplyMarkup;
import io.github.telegrambot.model.ParseMode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SendMessageRequest(
        @JsonProperty("chat_id")
        String chatId,

        String text,

        @JsonProperty("parse_mode")
        ParseMode parseMode,

        @JsonProperty("reply_markup")
        ReplyMarkup replyMarkup
) {
    public SendMessageRequest(
            String chatId,
            String text,
            ReplyMarkup replyMarkup
    ) {
        this(chatId, text, null, replyMarkup);
    }

}
