package io.github.solmosov.telegrambot.request.message.media;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class SendVideoRequest extends MediaRequest {

    @JsonProperty("video")
    private final String video;

    @JsonProperty("duration")
    private final Integer duration;

    @JsonProperty("width")
    private final Integer width;

    @JsonProperty("height")
    private final Integer height;

    @JsonProperty("has_spoiler")
    private final Boolean hasSpoiler;

    @JsonProperty("show_caption_above_media")
    private final Boolean showCaptionAboveMedia;

    private SendVideoRequest(Builder builder) {
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

        private String video;
        private Integer duration;
        private Integer width;
        private Integer height;
        private Boolean hasSpoiler;
        private Boolean showCaptionAboveMedia;

        private Builder() {
        }

        public Builder video(String url) {
            this.video = url;
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

        public Builder hasSpoiler() {
            this.hasSpoiler = true;
            return this;
        }

        public Builder showCaptionAboveMedia() {
            this.showCaptionAboveMedia = true;
            return this;
        }

        @Override
        public SendVideoRequest build() {
            return new SendVideoRequest(this);
        }
    }
}
