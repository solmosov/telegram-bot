package io.github.shahbozolmosov.telegrambot.request.message.caption;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.telegrambot.model.InputFIle;

public class SendPhotoUploadRequest extends SendMediaRequest {

    @JsonProperty("photo")
    private final InputFIle photo;

    @JsonProperty("has_spoiler")
    private final Boolean hasSpoiler;

    @JsonProperty("show_caption_above_media")
    private final Boolean showCaptionAboveMedia;

    private SendPhotoUploadRequest(Builder builder) {
        super(builder);
        this.photo = builder.photo;
        this.hasSpoiler = builder.hasSpoiler;
        this.showCaptionAboveMedia = builder.showCaptionAboveMedia;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends SendMediaRequest.Builder<Builder> {
        private InputFIle photo;
        private Boolean hasSpoiler;
        private Boolean showCaptionAboveMedia;

        private Builder() {
        }

        public Builder photo(InputFIle photo) {
            this.photo = photo;
            return this;
        }

        public Builder hasSpoiler(boolean value) {
            this.hasSpoiler = value;
            return this;
        }

        public Builder showCaptionAboveMedia(boolean value) {
            this.showCaptionAboveMedia = value;
            return this;
        }

        @Override
        public SendPhotoUploadRequest build() {
            return new SendPhotoUploadRequest(this);
        }
    }
}
