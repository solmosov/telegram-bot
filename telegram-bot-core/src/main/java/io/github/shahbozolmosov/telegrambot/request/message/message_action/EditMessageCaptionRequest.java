package io.github.shahbozolmosov.telegrambot.request.message.message_action;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.model.ParseMode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EditMessageCaptionRequest(
        @JsonProperty("chat_id")
        String chatId,

        @JsonProperty("message_id")
        String messageId,

        String text,

        @JsonProperty("parse_mode")
        ParseMode parseMode,

        @JsonProperty("reply_markup")
        ReplyMarkup replyMarkup,

        @JsonProperty("disable_web_page_preview")
        Boolean disableWebPagePreview
) {
}
