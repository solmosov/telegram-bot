package io.github.shahbozolmosov.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Contact(
        @JsonProperty("phone_number")
        String phoneNumber,

        @JsonProperty("first_name")
        String firstName,
        
        String vcard,

        @JsonProperty("user_id")
        long userId
) {
}
