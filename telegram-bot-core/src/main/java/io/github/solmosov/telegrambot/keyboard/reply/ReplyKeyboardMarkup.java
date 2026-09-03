package io.github.solmosov.telegrambot.keyboard.reply;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReplyKeyboardMarkup(
        List<List<ReplyKeyboardButton>> keyboard,

        @JsonProperty("is_persistent")
        Boolean isPersistent,

        @JsonProperty("resize_keyboard")
        Boolean resizeKeyboard,

        @JsonProperty("one_time_keyboard")
        Boolean oneTimeKeyboard,

        @JsonProperty("input_field_placeholder")
        String inputFieldPlaceholder,

        @JsonProperty("selective")
        Boolean selective,

        @JsonProperty("remove_keyboard")
        Boolean remove_keyboard

) implements ReplyMarkup {

}
