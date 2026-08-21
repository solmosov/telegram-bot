package io.github.shahbozolmosov.exception.webhook;

import io.github.shahbozolmosov.exception.TelegramBotException;

public class RequestBodyTooLargeException extends TelegramBotException {
    public RequestBodyTooLargeException() {
        super("Request body is too large");
    }
}
