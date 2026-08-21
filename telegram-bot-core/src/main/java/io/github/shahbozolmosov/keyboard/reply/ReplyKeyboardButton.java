package io.github.shahbozolmosov.keyboard.reply;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.net.httpserver.Request;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReplyKeyboardButton(
        String text,

        @JsonProperty("request_location")
        Boolean requestLocation,

        @JsonProperty("request_contact")
        Boolean requestContact,

        @JsonProperty("request_users")
        RequestUsers requestUsers
) implements ReplyKeyboardElement {

    public ReplyKeyboardButton(
            String text
    ) {
        this(text, null, null, null);
    }

    public ReplyKeyboardButton(
            String text,
            boolean requestLocation
    ) {
        this(text, requestLocation, null, null);
    }

    public static ReplyKeyboardButton contact(String text) {
        return new ReplyKeyboardButton(
                text,
                null,
                true,
                null
        );
    }

    public static ReplyKeyboardButton requestUsers(String text, RequestUsers requestUsers) {
        return new ReplyKeyboardButton(
                text,
                null,
                null,
                requestUsers
        );
    }
}
