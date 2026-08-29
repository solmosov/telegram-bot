package io.github.solmosov.telegrambot.request.message.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkPreviewOptions {

    @JsonProperty("is_disabled")
    Boolean isDisabled;

    @JsonProperty("url")
    String url;

    @JsonProperty("prefer_small_media")
    Boolean preferSmallMedia;

    @JsonProperty("prefer_large_media")
    Boolean preferLargeMedia;

    @JsonProperty("show_above_text")
    Boolean showAboveText;

    public LinkPreviewOptions(
            Builder builder
    ) {
        this.isDisabled = builder.isDisabled;
        this.url = builder.url;
        this.preferSmallMedia = builder.preferSmallMedia;
        this.preferLargeMedia = builder.preferLargeMedia;
        this.showAboveText = builder.showAboveText;
    }

    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getPreferSmallMedia() {
        return preferSmallMedia;
    }

    public Boolean getPreferLargeMedia() {
        return preferLargeMedia;
    }

    public Boolean getShowAboveText() {
        return showAboveText;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Boolean isDisabled;
        private String url;
        private Boolean preferSmallMedia;
        private Boolean preferLargeMedia;
        private Boolean showAboveText;

        private Builder() {
        }

        public Builder isDisabled(Boolean value){
            this.isDisabled = value;
            return this;
        }

        public Builder url(String url){
            this.url = url;
            return this;
        }

        public Builder preferSmallMedia(Boolean value){
            this.preferSmallMedia = value;
            return this;
        }

        public Builder preferLargeMedia(Boolean value){
            this.preferLargeMedia = value;
            return this;
        }

        public Builder showAboveText(Boolean value){
            this.showAboveText = value;
            return this;
        }

        public LinkPreviewOptions build() {
            return new LinkPreviewOptions(this);
        }
    }
}