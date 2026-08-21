package io.github.telegrambot.exception.api;

import io.github.telegrambot.exception.TelegramBotException;

public class TelegramApiException extends TelegramBotException {

    private final Integer errorCode;

    public TelegramApiException(Integer errorCode, String description) {
        super(description);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
