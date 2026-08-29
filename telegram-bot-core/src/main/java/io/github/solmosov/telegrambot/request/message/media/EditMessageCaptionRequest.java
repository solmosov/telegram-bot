package io.github.solmosov.telegrambot.request.message.media;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditMessageCaptionRequest extends MediaRequest {

    @JsonProperty("message_id")
    private final long messageId;

    public EditMessageCaptionRequest(Builder builder) {
        super(builder);
        this.messageId = builder.messageId;
    }

    public long getMessageId(){
        return messageId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends MediaRequest.Builder<Builder> {

        private long messageId;

        private Builder() {
        }

        public Builder messageId(long messageId) {
            this.messageId = messageId;
            return this;
        }

        @Override
        public EditMessageCaptionRequest build() {
            return new EditMessageCaptionRequest(this);
        }
    }
}
