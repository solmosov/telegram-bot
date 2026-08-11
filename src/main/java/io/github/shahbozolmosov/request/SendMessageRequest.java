package io.github.shahbozolmosov.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.keyboard.inline.InlineKeyboardMarkup;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboardMarkup;
import io.github.shahbozolmosov.model.ParseMode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SendMessageRequest(
        @JsonProperty("chat_id")
        long chatId,

        String text,

        @JsonProperty("parse_mode")
        ParseMode parseMode,

        @JsonProperty("reply_markup")
        ReplyMarkup replyMarkup
) {
    public SendMessageRequest(
            long chatId,
            String text,
            ReplyMarkup replyMarkup
    ) {
        this(chatId, text, null, replyMarkup);
    }

}
