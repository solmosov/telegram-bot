package io.github.solmosov.telegrambot.keyboard.reply.button;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class RequestUsers {
    @JsonProperty("request_id")
    private final int requestId;

    @JsonProperty("user_is_bot")
    private final Boolean userIsBot;

    @JsonProperty("user_is_premium")
    private final Boolean userIsPremium;

    @JsonProperty("max_quantity")
    private final Integer maxQuantity;

    @JsonProperty("request_name")
    private final Boolean requestName;

    @JsonProperty("request_username")
    private final Boolean requestUsername;

    @JsonProperty("request_photo")
    private final Boolean requestPhoto;

    private RequestUsers(Builder builder) {
        this.requestId = builder.requestId;
        this.userIsBot = builder.userIsBot;
        this.requestName = builder.requestName;
        this.requestUsername = builder.requestUsername;
        this.requestPhoto = builder.requestPhoto;
        this.userIsPremium = builder.userIsPremium;
        this.maxQuantity = builder.maxQuantity;
    }

    public static Builder builder(int requestId) {
        return new Builder(requestId);
    }

    public static class Builder {
        private final int requestId;
        private Boolean userIsBot;
        private Boolean requestName;
        private Boolean requestUsername;
        private Boolean requestPhoto;
        private Boolean userIsPremium;
        private Integer maxQuantity;


        private Builder(int requestId) {
            this.requestId = requestId;
        }

        public Builder userIsBot() {
            this.userIsBot = true;
            return this;
        }

        public Builder requestName() {
            this.requestName = true;
            return this;
        }

        public Builder requestUsername() {
            this.requestUsername = true;
            return this;
        }

        public Builder requestPhoto() {
            this.requestPhoto = true;
            return this;
        }

        public Builder userIsPremium() {
            this.userIsPremium = true;
            return this;
        }

        public Builder maxQuantity(Integer maxQuantity) {
            this.maxQuantity = maxQuantity;
            return this;
        }

        public RequestUsers build() {
            return new RequestUsers(this);
        }
    }
}
