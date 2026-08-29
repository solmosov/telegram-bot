package io.github.solmosov.telegrambot.keyboard.inline;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;

import java.util.List;

public record InlineKeyboardMarkup(
        @JsonProperty("inline_keyboard")
        List<List<InlineKeyboardButton>> inlineKeyboard
) implements ReplyMarkup {
    public static InlineKeyboardMarkup remove() {
        return new InlineKeyboardMarkup(List.of());
    }
}
