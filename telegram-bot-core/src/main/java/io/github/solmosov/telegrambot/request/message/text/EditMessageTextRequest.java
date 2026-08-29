package io.github.solmosov.telegrambot.request.message.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditMessageTextRequest extends TextRequest {
    @JsonProperty("message_id")
    private final long messageId;

    public EditMessageTextRequest(Builder builder) {
        super(builder);

        this.messageId = builder.messageId;
    }

    public long getMessageId(){
        return messageId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TextRequest.Builder<Builder> {

        private long messageId;

        public Builder messageId(long messageId) {
            this.messageId = messageId;
            return this;
        }

        @Override
        public EditMessageTextRequest build() {
            return new EditMessageTextRequest(this);
        }
    }
}
