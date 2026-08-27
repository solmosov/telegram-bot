package io.github.shahbozolmosov.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Message(
        @JsonProperty("message_id")
        String messageId,

        From from,
        Chat chat,
        Long date,
        String text,

        List<PhotoSize> photo,
        DocumentInfo document,
        String caption,

        @JsonProperty("reply_to_message")
        ReplyToMessage replyToMessage,

        Location location,

        Contact contact,

        @JsonProperty("users_shared")
        UsersShared usersShared
) {
}
