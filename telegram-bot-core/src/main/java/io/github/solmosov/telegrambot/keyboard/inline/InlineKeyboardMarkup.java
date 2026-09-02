package io.github.solmosov.telegrambot.keyboard.inline;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record InlineKeyboardMarkup(
        @JsonProperty("inline_keyboard")
        List<List<InlineKeyboardButton>> inlineKeyboard
) implements ReplyMarkup {
    public static InlineKeyboardMarkup remove() {
        return new InlineKeyboardMarkup(null);
    }
}
