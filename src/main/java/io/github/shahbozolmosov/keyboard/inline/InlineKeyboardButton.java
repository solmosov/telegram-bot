package io.github.shahbozolmosov.keyboard.inline;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.nio.charset.StandardCharsets;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record InlineKeyboardButton(
        String text,

        @JsonProperty("callback_data")
        String callbackData,

        Style style,

        String url
) implements InlineKeyboardElement {

    public enum Style {
        PRIMARY("primary"),
        SUCCESS("success"),
        DANGER("danger"),
        DEFAULT("default");


        private final String value;

        Style(String value) {
            this.value = value;
        }

        @JsonValue
        public String value() {
            return value;
        }

        public String toApiValue() {
            return value;
        }
    }

    public InlineKeyboardButton {
        if (callbackData != null) {
            int byteLength = callbackData.getBytes(StandardCharsets.UTF_8).length;

            if (byteLength > 64) {
                throw new IllegalArgumentException(
                        "Callback data exceeds Telegram's 64-byte limit: "
                                + byteLength + " bytes"
                );
            }
        }
    }
}
