package io.github.shahbozolmosov.telegrambot.keyboard.inline;

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

        String url,

        @JsonProperty("web_app")
        WebApp webApp
) implements InlineKeyboardElement {


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

    public static InlineKeyboardButton callback(String text, String callbackData, Style style) {
        return new InlineKeyboardButton(
                text,
                callbackData,
                style,
                null,
                null
        );
    }

    public static InlineKeyboardButton url(String text, String url, Style style) {
        return new InlineKeyboardButton(
                text,
                null,
                style,
                url,
                null
        );
    }


    public static InlineKeyboardButton webApp(String text, String webAppUrl, Style style) {
        return new InlineKeyboardButton(
                text,
                null,
                style,
                null,
                new WebApp(webAppUrl)
        );
    }

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

    private record WebApp(
            String url
    ) {
    }
}
