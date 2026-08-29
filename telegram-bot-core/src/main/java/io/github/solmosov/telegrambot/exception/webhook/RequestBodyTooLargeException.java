package io.github.solmosov.telegrambot.exception.webhook;

import io.github.solmosov.telegrambot.exception.TelegramBotException;

public class RequestBodyTooLargeException extends TelegramBotException {
    public RequestBodyTooLargeException() {
        super("Request body is too large");
    }
}
