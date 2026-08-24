package io.github.shahbozolmosov.telegrambot.request.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.keyboard.reply.ReplyKeyboardMarkup;
import io.github.shahbozolmosov.telegrambot.model.ParseMode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SendMessageRequest(
        @JsonProperty("chat_id")
        String chatId,

        String text,

        @JsonProperty("parse_mode")
        ParseMode parseMode,

        @JsonProperty("reply_markup")
        ReplyMarkup replyMarkup,

        @JsonProperty("disable_web_page_preview")
        Boolean disableWebPagePreview
) {
    /*------------------------------- TEXT --------------------------------------------*/
    public static SendMessageRequest text(String chatId, String text, ReplyMarkup replyMarkup, Boolean disableWebPagePreview) {
        return new SendMessageRequest(
                chatId,
                text,
                null,
                replyMarkup,
                disableWebPagePreview
        );
    }

    /*------------------------------- Markdown --------------------------------------------*/
    public static SendMessageRequest markdown(String chatId, String text, ReplyMarkup replyMarkup, Boolean disableWebPagePreview) {
        return new SendMessageRequest(
                chatId,
                text,
                ParseMode.MARKDOWN,
                replyMarkup,
                disableWebPagePreview
        );
    }

    /*------------------------------- Markdown V2 --------------------------------------------*/

    public static SendMessageRequest markdownV2(String chatId, String text, ReplyMarkup replyMarkup, Boolean disableWebPagePreview) {
        return new SendMessageRequest(
                chatId,
                text,
                ParseMode.MARKDOWN_V2,
                replyMarkup,
                disableWebPagePreview
        );
    }

    /*------------------------------- Html --------------------------------------------*/

    public static SendMessageRequest html(String chatId, String text, ReplyMarkup replyMarkup, Boolean disableWebPagePreview) {
        return new SendMessageRequest(
                chatId,
                text,
                ParseMode.HTML,
                replyMarkup,
                disableWebPagePreview
        );
    }

    /*------------------------------- Remove Keyboard --------------------------------------------*/
    public static SendMessageRequest removeReplyKeyboard(String chatId, String text){
        return new SendMessageRequest(
                chatId,
                text,
                null,
                ReplyKeyboardMarkup.remove(),
                null
        );
    }

}
