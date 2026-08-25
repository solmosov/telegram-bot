package io.github.shahbozolmosov.telegrambot.request.message.media;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.model.ParseMode;
import io.github.shahbozolmosov.telegrambot.request.message.AbstractRequest;

@JsonInclude(JsonInclude.Include.NON_NULL)
abstract class MediaRequest extends AbstractRequest {

    @JsonProperty("caption")
    final String caption;

    @JsonProperty("parse_mode")
    final ParseMode parseMode;

    @JsonProperty("reply_markup")
    final ReplyMarkup replyMarkup;

    public MediaRequest(Builder<?> builder) {
        super(builder);
        this.caption = builder.caption;
        this.parseMode = builder.parseMode;
        this.replyMarkup = builder.replyMarkup;
    }

    public abstract static class Builder<T extends Builder<T>> extends AbstractRequest.Builder<T> {
        private String caption;
        private ParseMode parseMode;
        private ReplyMarkup replyMarkup;

        @SuppressWarnings("unchecked")
        public T self() {
            return (T) this;
        }

        public T caption(String caption) {
            this.caption = caption;
            return self();
        }

        public T html() {
            this.parseMode = ParseMode.HTML;
            return self();
        }

        public T markdown() {
            this.parseMode = ParseMode.MARKDOWN;
            return self();
        }

        public T markdownV2() {
            this.parseMode = ParseMode.MARKDOWN_V2;
            return self();
        }

        public T replyMarkup(ReplyMarkup replyMarkup) {
            this.replyMarkup = replyMarkup;
            return self();
        }

        public abstract MediaRequest build();
    }
}
