package io.github.shahbozolmosov.telegrambot.exception.client;

import io.github.shahbozolmosov.telegrambot.exception.TelegramBotException;

public class TelegramClientException extends TelegramBotException {

    public TelegramClientException(String message) {
        super(message);
    }

    public TelegramClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
