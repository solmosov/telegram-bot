package io.github.shahbozolmosov.telegrambot.exception.webhook;

import io.github.shahbozolmosov.telegrambot.exception.TelegramBotException;

public class RequestBodyTooLargeException extends TelegramBotException {
    public RequestBodyTooLargeException() {
        super("Request body is too large");
    }
}
