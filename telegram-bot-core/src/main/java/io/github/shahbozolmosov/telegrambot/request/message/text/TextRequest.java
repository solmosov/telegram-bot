package io.github.shahbozolmosov.telegrambot.request.message.text;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.model.ParseMode;
import io.github.shahbozolmosov.telegrambot.request.message.AbstractRequest;

abstract class TextRequest extends AbstractRequest {
    @JsonProperty("text")
    private String text;

    @JsonProperty("parse_mode")
    private ParseMode parseMode;

    @JsonProperty("reply_markup")
    private ReplyMarkup replyMarkup;


    public TextRequest(String chatId, Boolean allowPaidBroadcast, Boolean protectContent, Boolean disableNotification) {
        super(chatId, allowPaidBroadcast, protectContent, disableNotification);
    }
}
