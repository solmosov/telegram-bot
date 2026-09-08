package io.github.solmosov.telegrambot.request.message.media;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class SendAudioRequest extends MediaRequest {

    @JsonProperty("audio")
    private final String audio;

    @JsonProperty("duration")
    private final Integer duration;


    public SendAudioRequest(Builder builder) {
        super(builder);
        this.audio = builder.audio;
        this.duration = builder.duration;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends MediaRequest.Builder<Builder> {

        private String audio;
        private Integer duration;

        private Builder() {

        }

        public Builder audio(String audio) {
            this.audio = audio;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        @Override
        public SendAudioRequest build() {
            return new SendAudioRequest(this);
        }
    }
}
