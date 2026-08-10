package io.github.shahbozolmosov.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InlineKeyboardButton(
        String text,

        @JsonProperty("callback_data")
        String callbackData
) {
}
