package io.github.shahbozolmosov.telegrambot.request.message.media;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.telegrambot.model.InputFIle;

public final class SendVideoUploadRequest extends MediaRequest {

    @JsonProperty("video")
    private final InputFIle video;

    @JsonProperty("duration")
    private final Integer duration;

    @JsonProperty("width")
    private final Integer width;

    @JsonProperty("height")
    private final Integer height;

    @JsonProperty("has_spoiler")
    private final boolean hasSpoiler;

    @JsonProperty("show_caption_above_media")
    private final boolean showCaptionAboveMedia;

    private SendVideoUploadRequest(Builder builder) {
        super(builder);
        this.video = builder.video;
        this.duration = builder.duration;
        this.width = builder.width;
        this.height = builder.height;
        this.hasSpoiler = builder.hasSpoiler;
        this.showCaptionAboveMedia = builder.showCaptionAboveMedia;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends MediaRequest.Builder<Builder> {

        private InputFIle video;
        private Integer duration;
        private Integer width;
        private Integer height;
        private boolean hasSpoiler;
        private boolean showCaptionAboveMedia;

        private Builder() {
        }

        public Builder video(InputFIle file) {
            this.video = file;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
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
        public SendVideoUploadRequest build() {
            return new SendVideoUploadRequest(this);
        }
    }
}
