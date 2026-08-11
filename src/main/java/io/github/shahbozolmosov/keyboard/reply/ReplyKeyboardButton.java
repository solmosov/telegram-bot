package io.github.shahbozolmosov.keyboard.reply;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReplyKeyboardButton(
        String text,

        @JsonProperty("request_location")
        Boolean requestLocation,

        @JsonProperty("request_contact")
        Boolean requestContact
) implements ReplyKeyboardElement {

    public ReplyKeyboardButton(
            String text
    ) {
        this(text, null, null);
    }

    public ReplyKeyboardButton(
            String text,
            boolean requestLocation
    ) {
        this(text, requestLocation, null);
    }

}
