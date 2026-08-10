package io.github.shahbozolmosov.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.keyboard.inline.InlineKeyboardMarkup;
import io.github.shahbozolmosov.model.ParseMode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EditMessageRequest(
        @JsonProperty("chat_id")
        long chatId,

        @JsonProperty("message_id")
        long messageId,

        String text,

        @JsonProperty("parse_mode")
        ParseMode parseMode,

        @JsonProperty("reply_markup")
        InlineKeyboardMarkup replyMarkup
) {
    public EditMessageRequest(
            long chatId,
            long messageId,
            String text,
            InlineKeyboardMarkup replyMarkup
    ) {
        this(chatId, messageId, text, null, replyMarkup);
    }
}
