package io.github.shahbozolmosov.telegrambot.request.message.text;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendMessageRequest extends TextRequest {

    public SendMessageRequest(Builder builder) {
        super(builder);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends TextRequest.Builder<Builder> {

        @Override
        public SendMessageRequest build() {
            return new SendMessageRequest(this);
        }
    }
}
