package io.github.shahbozolmosov.telegrambot.request.message.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.model.ParseMode;
import io.github.shahbozolmosov.telegrambot.request.message.AbstractMessageRequest;

@JsonInclude(JsonInclude.Include.NON_NULL)
abstract class TextRequest extends AbstractMessageRequest {
    @JsonProperty("text")
    final String text;

    @JsonProperty("parse_mode")
    final ParseMode parseMode;

    @JsonProperty("reply_markup")
    final ReplyMarkup replyMarkup;


    public TextRequest(Builder<?> builder) {
        super(builder);
        this.text = builder.text;
        this.parseMode = builder.parseMode;
        this.replyMarkup = builder.replyMarkup;
    }

    public abstract static class Builder<T extends Builder<T>> extends AbstractMessageRequest.Builder<T> {
        private String text;
        private ParseMode parseMode;
        private ReplyMarkup replyMarkup;

        @SuppressWarnings("unchecked")
        protected T self() {
            return (T) this;
        }

        public T text(String text) {
            this.text = text;
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

        public abstract TextRequest build();
    }
}
