package io.github.solmosov.telegrambot.request.message.media;


import com.fasterxml.jackson.annotation.JsonProperty;

public final class SendDocumentRequest extends MediaRequest {

    @JsonProperty("document")
    private final String document;

    @JsonProperty("disable_content_type_detection")
    private final Boolean disableContentTypeDetection;

    private SendDocumentRequest(Builder builder) {
        super(builder);
        this.document = builder.document;
        this.disableContentTypeDetection = builder.disableContentTypeDetection;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends MediaRequest.Builder<Builder> {
        private String document;
        private Boolean disableContentTypeDetection;

        private Builder() {
        }

        public Builder document(String document) {
            this.document = document;
            return this;
        }

        public Builder disableContentTypeDetection(boolean value) {
            this.disableContentTypeDetection = value;
            return this;
        }

        @Override
        public SendDocumentRequest build() {
            return new SendDocumentRequest(this);
        }
    }
}
