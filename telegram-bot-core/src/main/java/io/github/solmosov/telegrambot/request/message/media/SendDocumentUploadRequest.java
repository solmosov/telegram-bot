package io.github.solmosov.telegrambot.request.message.media;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.solmosov.telegrambot.model.InputFile;

public final class SendDocumentUploadRequest extends MediaRequest {

    @JsonProperty("document")
    private final InputFile document;

    @JsonProperty("disable_content_type_detection")
    private final Boolean disableContentTypeDetection;

    SendDocumentUploadRequest(Builder builder) {
        super(builder);
        this.document = builder.document;
        this.disableContentTypeDetection = builder.disableContentTypeDetection;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends MediaRequest.Builder<Builder> {
        private InputFile document;
        private Boolean disableContentTypeDetection;


        private Builder() {
        }

        public Builder document(InputFile document){
            this.document = document;
            return this;
        }

        public Builder disableContentTypeDetection(boolean value){
            this.disableContentTypeDetection = value;
            return this;
        }

        @Override
        public SendDocumentUploadRequest build() {
            return new SendDocumentUploadRequest(this);
        }
    }
}
