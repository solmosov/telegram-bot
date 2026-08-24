package io.github.shahbozolmosov.telegrambot.request.media.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.telegrambot.model.InputFIle;

public class SendPhotoUploadRequest extends SendMediaRequest {

    @JsonProperty("photo")
    private final InputFIle photo;

    @JsonProperty("has_spoiler")
    private final Boolean hasSpoiler;

    private SendPhotoUploadRequest(Builder builder) {
        super(builder);
        this.photo = builder.photo;
        this.hasSpoiler = builder.hasSpoiler;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends SendMediaRequest.Builder<Builder> {
        private InputFIle photo;
        private Boolean hasSpoiler;

        private Builder() {
        }

        public Builder photo(InputFIle photo) {
            this.photo = photo;
            return this;
        }

        public Builder hasSpoiler(boolean hasSpoiler) {
            this.hasSpoiler = hasSpoiler;
            return this;
        }

        @Override
        public SendPhotoUploadRequest build() {
            return new SendPhotoUploadRequest(this);
        }
    }
}
