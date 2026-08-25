package io.github.shahbozolmosov.telegrambot.request.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.telegrambot.request.message.options.LinkPreviewOptions;

import java.util.function.Consumer;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractRequest {

    @JsonProperty("chat_id")
    protected final String chatId;

    @JsonProperty("allow_paid_broadcast")
    protected final Boolean allowPaidBroadcast;

    @JsonProperty("protect_content")
    protected final Boolean protectContent;

    @JsonProperty("disable_notification")
    protected final Boolean disableNotification;

    @JsonProperty("link_preview_options")
    protected final LinkPreviewOptions linkPreviewOptions;

    @JsonProperty("reply_parameters")


    public AbstractRequest(Builder<?> builder) {
        this.chatId = builder.chatId;
        this.allowPaidBroadcast = builder.allowPaidBroadcast;
        this.protectContent = builder.protectContent;
        this.disableNotification = builder.disableNotification;
        this.linkPreviewOptions = builder.linkPreviewOptions;
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

    public LinkPreviewOptions getLinkPreviewOptions() {
        return linkPreviewOptions;
    }

    public abstract static class Builder<T extends Builder<T>> {
        private String chatId;
        private Boolean allowPaidBroadcast;
        private Boolean protectContent;
        private Boolean disableNotification;
        private LinkPreviewOptions linkPreviewOptions;

        @SuppressWarnings("unchecked")
        protected T self() {
            return (T) this;
        }

        public T chatId(String chatId) {
            this.chatId = chatId;
            return self();
        }

        public T allowPAidBroadcast(Boolean value) {
            this.allowPaidBroadcast = value;
            return self();
        }

        public T protectContent(Boolean value) {
            this.protectContent = value;
            return self();
        }

        public T disableNotification(Boolean value) {
            this.disableNotification = value;
            return self();
        }

        public T linkPreviewOptions(Consumer<LinkPreviewOptions.Builder> consumer) {
            LinkPreviewOptions.Builder builder = LinkPreviewOptions.builder();

            consumer.accept(builder);

            this.linkPreviewOptions = builder.build();

            return self();
        }

        public abstract AbstractRequest build();
    }
}