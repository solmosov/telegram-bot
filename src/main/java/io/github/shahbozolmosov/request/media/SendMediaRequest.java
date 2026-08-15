package io.github.shahbozolmosov.request.media;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.model.ParseMode;
import io.github.shahbozolmosov.model.ReplyParameters;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class SendMediaRequest {

    @JsonProperty("chat_id")
    protected final String chatId;

    @JsonProperty("business_connection_id")
    protected final String businessConnectionId;

    @JsonProperty("message_thread_id")
    protected final Integer messageThreadId;

    @JsonProperty("caption")
    protected final String caption;

    @JsonProperty("parse_mode")
    protected final ParseMode parseMode;

    @JsonProperty("disable_notification")
    protected final Boolean disableNotification;

    @JsonProperty("protect_content")
    protected final Boolean protectContent;

    @JsonProperty("allow_paid_broadcast")
    protected final Boolean allowPaidBroadcast;

    @JsonProperty("message_effect_id")
    protected final String messageEffectId;

    @JsonProperty("reply_parameters")
    protected final ReplyParameters replyParameters;

    @JsonProperty("reply_markup")
    protected final ReplyMarkup replyMarkup;

    public String chatId() {
        return chatId;
    }

    protected SendMediaRequest(Builder<?> builder) {
        this.chatId = builder.chatId;
        this.businessConnectionId = builder.businessConnectionId;
        this.messageThreadId = builder.messageThreadId;
        this.caption = builder.caption;
        this.parseMode = builder.parseMode;
        this.disableNotification = builder.disableNotification;
        this.protectContent = builder.protectContent;
        this.allowPaidBroadcast = builder.allowPaidBroadcast;
        this.messageEffectId = builder.messageEffectId;
        this.replyParameters = builder.replyParameters;
        this.replyMarkup = builder.replyMarkup;
    }

    @SuppressWarnings("unchecked")
    public abstract static class Builder<T extends Builder<T>> {
        private String chatId;
        private String businessConnectionId;
        private Integer messageThreadId;
        private String caption;
        private ParseMode parseMode;
        private Boolean disableNotification;
        private Boolean protectContent;
        private Boolean allowPaidBroadcast;
        private String messageEffectId;
        private ReplyParameters replyParameters;
        private ReplyMarkup replyMarkup;

        protected T self() {
            return (T) this;
        }

        public T chatId(String chatId) {
            this.chatId = chatId;
            return self();
        }

        public T businessConnectionId(String businessConnectionId) {
            this.businessConnectionId = businessConnectionId;
            return self();
        }

        public T messageThreadId(Integer messageThreadId) {
            this.messageThreadId = messageThreadId;
            return self();
        }

        public T caption(String caption) {
            this.caption = caption;
            return self();
        }

        public T parseMode(ParseMode parseMode) {
            this.parseMode = parseMode;
            return self();
        }

        public T disableNotification(Boolean disableNotification) {
            this.disableNotification = disableNotification;
            return self();
        }

        public T protectContent(Boolean protectContent) {
            this.protectContent = protectContent;
            return self();
        }

        public T allowPaidBroadcast(Boolean allowPaidBroadcast) {
            this.allowPaidBroadcast = allowPaidBroadcast;
            return self();
        }

        public T messageEffectId(String messageEffectId) {
            this.messageEffectId = messageEffectId;
            return self();
        }

        public T replyParameters(ReplyParameters replyParameters) {
            this.replyParameters = replyParameters;
            return self();
        }

        public T replyMarkup(ReplyMarkup replyMarkup) {
            this.replyMarkup = replyMarkup;
            return self();
        }

        /*----------------Helper methods-------------------------*/
        public T html(String html) {
            this.caption = html;
            this.parseMode = ParseMode.HTML;
            return self();
        }

        public T markdown(String markdown) {
            this.caption = markdown;
            this.parseMode = ParseMode.MARKDOWN;
            return self();
        }

        public T markdownV2(String markdownV2) {
            this.caption = markdownV2;
            this.parseMode = ParseMode.MARKDOWN_V2;
            return self();
        }

        public abstract SendMediaRequest build();
    }
}