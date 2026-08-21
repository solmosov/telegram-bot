package io.github.telegrambot.exception.webhook;

import io.github.telegrambot.exception.TelegramBotException;

public class RequestBodyTooLargeException extends TelegramBotException {
    public RequestBodyTooLargeException() {
        super("Request body is too large");
    }
}
