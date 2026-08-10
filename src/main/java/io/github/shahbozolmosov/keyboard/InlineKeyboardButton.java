package io.github.shahbozolmosov.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.nio.charset.StandardCharsets;

public record InlineKeyboardButton(
        String text,

        @JsonProperty("callback_data")
        String callbackData
) implements KeyboardElement {

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
