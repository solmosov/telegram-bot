package io.github.solmosov.telegrambot.keyboard.reply;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;

import java.util.List;
import java.util.function.Consumer;

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
        Boolean removeKeyboard

) implements ReplyMarkup {
    public static ReplyKeyboardMarkup remove(Boolean selective) {
        return new ReplyKeyboardMarkup(
                null,
                null,
                null,
                null,
                null,
                selective,
                true
        );
    }

    public static ReplyKeyboardMarkup initial(List<List<ReplyKeyboardButton>> keyboard){
        return new ReplyKeyboardMarkup(
                keyboard,
                null,
                true,
                null,
                null,
                null,
                null
        );
    }

    public ReplyKeyboardMarkup options(Consumer<Options> consumer) {
        Options options = new Options();
        consumer.accept(options);

        return new ReplyKeyboardMarkup(
                keyboard,
                options.isPersistent,
                options.resizeKeyboard,
                options.oneTimeKeyboard,
                options.inputFieldPlaceholder,
                options.selective,
                removeKeyboard
        );
    }

    public static class Options {
        private Boolean isPersistent;
        private Boolean resizeKeyboard = true;
        private Boolean oneTimeKeyboard;
        private String inputFieldPlaceholder;
        private Boolean selective;

        public Options isPersistent() {
            this.isPersistent = true;
            return this;
        }

        public Options resizeKeyboard() {
            this.resizeKeyboard = true;
            return this;
        }

        public Options oneTimeKeyboard() {
            this.oneTimeKeyboard = true;
            return this;
        }

        public Options inputFieldPlaceholder(String value) {
            this.inputFieldPlaceholder = value;
            return this;
        }

        public Options selective() {
            this.selective = true;
            return this;
        }
    }
}
