package io.github.shahbozolmosov.telegrambot.keyboard.reply;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RequestUsers(
        @JsonProperty("request_id")
        int requestId,

        @JsonProperty("user_is_bot")
        Boolean userIsBot,

        @JsonProperty("request_name")
        Boolean requestName,

        @JsonProperty("request_username")
        Boolean requestUsername,

        @JsonProperty("request_photo")
        Boolean requestPhoto,

        @JsonProperty("user_is_premium")
        Boolean userIsPremium,

        @JsonProperty("max_quantity")
        int maxQuantity
) {
    public static RequestUsers user(int requestId, int maxQuantity) {
        return new RequestUsers(
                requestId,
                false,
                true,
                true,
                false,
                null,
                maxQuantity
        );
    }

    public static RequestUsers userPremium(int requestId, int maxQuantity) {
        return new RequestUsers(
                requestId,
                false,
                true,
                true,
                false,
                true,
                maxQuantity
        );
    }

    public static RequestUsers userNonPremium(int requestId, int maxQuantity) {
        return new RequestUsers(
                requestId,
                false,
                true,
                true,
                false,
                false,
                maxQuantity
        );
    }

    public static RequestUsers userBots(int requestId, int maxQuantity) {
        return new RequestUsers(
                requestId,
                true,
                true,
                true,
                false,
                null,
                maxQuantity
        );
    }
}
