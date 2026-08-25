package io.github.shahbozolmosov.telegrambot.request.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractRequest {

    @JsonProperty("chat_id")
    String chatId;

    @JsonProperty("allow_paid_broadcast")
    Boolean allowPaidBroadcast;

    @JsonProperty("protect_content")
    Boolean protectContent;

    @JsonProperty("disable_notification")
    Boolean disableNotification;

    @JsonProperty("link_preview_options")
    LinkPreviewOptions linkPreviewOptions;

    public AbstractRequest(
            String chatId,
            Boolean allowPaidBroadcast,
            Boolean protectContent,
            Boolean disableNotification
    ) {
        this.chatId = chatId;
        this.allowPaidBroadcast = allowPaidBroadcast;
        this.protectContent = protectContent;
        this.disableNotification = disableNotification;
    }

    public String getChatId() {
        return chatId;
    }

    public Boolean getAllowPaidBroadcast() {
        return allowPaidBroadcast;
    }

    public Boolean getProtectContent() {
        return protectContent;
    }

    public Boolean getDisableNotification() {
        return disableNotification;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LinkPreviewOptions {

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
                Boolean isDisabled,
                String url,
                Boolean preferSmallMedia,
                Boolean preferLargeMedia,
                Boolean showAboveText
        ) {
            this.isDisabled = isDisabled;
            this.url = url;
            this.preferSmallMedia = preferSmallMedia;
            this.preferLargeMedia = preferLargeMedia;
            this.showAboveText = showAboveText;
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
    }
}