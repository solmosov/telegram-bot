package io.github.solmosov.telegrambot.keyboard.reply;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReplyKeyboardMarkup(
        List<List<ReplyKeyboardButton>> keyboard,

        @JsonProperty("resize_keyboard")
        Boolean resizeKeyboard,

        @JsonProperty("one_time_keyboard")
        Boolean oneTimeKeyboard,

        @JsonProperty("remove_keyboard")
        Boolean remove_keyboard
) implements ReplyMarkup {
    public ReplyKeyboardMarkup(List<List<ReplyKeyboardButton>> keyboard) {
        this(keyboard, true, true, null);
    }

    public static ReplyKeyboardMarkup remove() {
        return new ReplyKeyboardMarkup(
                null,
                null,
                null,
                true
        );
    }
}
