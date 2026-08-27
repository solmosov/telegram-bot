package io.github.shahbozolmosov.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TelegramResponse<T>(
        boolean ok,
        T result,

        @JsonProperty("error_code")
        Integer errorCode,
        String description
) {

}
