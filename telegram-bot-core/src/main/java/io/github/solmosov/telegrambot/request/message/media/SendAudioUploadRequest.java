package io.github.solmosov.telegrambot.request.message.media;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.solmosov.telegrambot.model.InputFile;

public final class SendAudioUploadRequest extends MediaRequest {

    @JsonProperty("audio")
    private final InputFile audio;

    @JsonProperty("duration")
    private final Integer duration;


    public SendAudioUploadRequest(Builder builder) {
        super(builder);
        this.audio = builder.audio;
        this.duration = builder.duration;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends MediaRequest.Builder<Builder> {

        private InputFile audio;
        private Integer duration;

        private Builder() {

        }

        public Builder audio(InputFile audio) {
            this.audio = audio;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        @Override
        public SendAudioUploadRequest build() {
            return new SendAudioUploadRequest(this);
        }
    }
}
