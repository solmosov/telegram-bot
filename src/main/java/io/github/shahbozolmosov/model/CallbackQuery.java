package io.github.shahbozolmosov.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CallbackQuery(
        String id,
        User from,
        Message message,

        @JsonProperty("chat_instance")
        String chatInstance,
        String data
) {

}
