package io.github.solmosov.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UsersShared(
        @JsonProperty("user_ids")
        List<Long> userIds,

        List<User> users,

        @JsonProperty("request_id")
        int requestId
) {

    public record User(
            @JsonProperty("user_id")
            long userId,

            @JsonProperty("first_name")
            String firstName,

            @JsonProperty("last_name")
            String lastName,

            String username,

            List<PhotoSize> photo
    ) {
    }
}
