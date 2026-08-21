package io.github.telegrambot.exception.client;

import io.github.telegrambot.exception.TelegramBotException;

public class TelegramClientException extends TelegramBotException {

    public TelegramClientException(String message) {
        super(message);
    }

    public TelegramClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
