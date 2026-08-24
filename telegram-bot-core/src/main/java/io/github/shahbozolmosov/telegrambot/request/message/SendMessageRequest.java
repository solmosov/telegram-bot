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
