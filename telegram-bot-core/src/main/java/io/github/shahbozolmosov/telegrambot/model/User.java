package io.github.shahbozolmosov.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record User(
        Long id,

        @JsonProperty("is_bot")
        Boolean isBot,

        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("last_name")
        String lastName,

        @JsonProperty("username")
        String username,

        @JsonProperty("language_code")
        String languageCode
) {
}
