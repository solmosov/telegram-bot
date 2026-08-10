package io.github.shahbozolmosov.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.keyboard.InlineKeyboardMarkup;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SendMessageRequest(
        @JsonProperty("chat_id")
        long chatId,

        String text,
        @JsonProperty("reply_markup")
        InlineKeyboardMarkup replyMarkup
) {
}
