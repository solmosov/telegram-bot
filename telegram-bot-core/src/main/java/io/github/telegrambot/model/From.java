package io.github.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record From(
        Long id,
        @JsonProperty("is_bot")
        boolean isBot,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("language_code")
        String languageCode
) {

}
