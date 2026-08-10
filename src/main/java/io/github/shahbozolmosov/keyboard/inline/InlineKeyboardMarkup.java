package io.github.shahbozolmosov.keyboard.inline;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record InlineKeyboardMarkup(
        @JsonProperty("inline_keyboard")
        List<List<InlineKeyboardButton>> inlineKeyboard
) {
}
