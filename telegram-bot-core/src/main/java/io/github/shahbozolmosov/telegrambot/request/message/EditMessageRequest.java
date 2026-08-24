package io.github.shahbozolmosov.telegrambot.request.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.keyboard.reply.ReplyKeyboardMarkup;
import io.github.shahbozolmosov.telegrambot.model.ParseMode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EditMessageRequest(
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
    /*------------------------------- TEXT --------------------------------------------*/
    public static EditMessageRequest text(String chatId, String messageId, String text, ReplyMarkup replyMarkup, Boolean disableWebPagePreview) {
        return new EditMessageRequest(
                chatId,
                messageId,
                text,
                null,
                replyMarkup,
                disableWebPagePreview
        );
    }

    /*------------------------------- Markdown --------------------------------------------*/
    public static EditMessageRequest markdown(String chatId, String messageId, String text, ReplyMarkup replyMarkup, Boolean disableWebPagePreview) {
        return new EditMessageRequest(
                chatId,
                messageId,
                text,
                ParseMode.MARKDOWN,
                replyMarkup,
                disableWebPagePreview
        );
    }

    /*------------------------------- Markdown V2 --------------------------------------------*/

    public static EditMessageRequest markdownV2(String chatId, String messageId, String text, ReplyMarkup replyMarkup, Boolean disableWebPagePreview) {
        return new EditMessageRequest(
                chatId,
                messageId,
                text,
                ParseMode.MARKDOWN_V2,
                replyMarkup,
                disableWebPagePreview
        );
    }

    /*------------------------------- Html --------------------------------------------*/

    public static EditMessageRequest html(String chatId, String messageId, String text, ReplyMarkup replyMarkup, Boolean disableWebPagePreview) {
        return new EditMessageRequest(
                chatId,
                messageId,
                text,
                ParseMode.HTML,
                replyMarkup,
                disableWebPagePreview
        );
    }

    /*------------------------------- Remove Keyboard --------------------------------------------*/
    public static EditMessageRequest removeReplyKeyboard(String chatId, String messageId, String text) {
        return new EditMessageRequest(
                chatId,
                messageId,
                text,
                null,
                ReplyKeyboardMarkup.remove(),
                null
        );
    }

}
