package io.github.shahbozolmosov.request.media.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.model.InputFIle;

public final class SendDocumentUploadRequest extends SendMediaRequest {

    @JsonProperty("document")
    private final InputFIle document;

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

    public static final class Builder extends SendMediaRequest.Builder<Builder> {
        private InputFIle document;
        private Boolean disableContentTypeDetection;


        private Builder() {
        }

        public Builder document(InputFIle document){
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
