package io.github.shahbozolmosov.keyboard.reply;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.keyboard.ReplyMarkup;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReplyKeyboardMarkup(
        List<List<ReplyKeyboardButton>> keyboard,

        @JsonProperty("resize_keyboard")
        boolean resizeKeyboard,

        @JsonProperty("one_time_keyboard")
        boolean oneTimeKeyboard
) implements ReplyMarkup {
    public ReplyKeyboardMarkup(List<List<ReplyKeyboardButton>> keyboard) {
        this(keyboard, true, true);
    }
}
