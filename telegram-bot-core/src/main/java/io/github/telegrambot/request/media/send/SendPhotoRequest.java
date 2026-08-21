package io.github.telegrambot.request.media.send;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class SendPhotoRequest extends SendMediaRequest {

    @JsonProperty("photo")
    private final String photo;

    @JsonProperty("has_spoiler")
    private final Boolean hasSpoiler;

    private SendPhotoRequest(Builder builder) {
        super(builder);
        this.photo = builder.photo;
        this.hasSpoiler = builder.hasSpoiler;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends SendMediaRequest.Builder<Builder> {

        private String photo;
        private Boolean hasSpoiler;


        private Builder() {
        }

        public Builder photo(String photo) {
            this.photo = photo;
            return this;
        }

        public Builder hasSpoiler(boolean value) {
            this.hasSpoiler = value;
            return this;
        }

        @Override
        public SendPhotoRequest build() {
            return new SendPhotoRequest(this);
        }
    }
}
