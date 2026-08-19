package io.github.shahbozolmosov.exception.client;

public class TelegramClientException extends RuntimeException {

    public TelegramClientException(String message) {
        super(message);
    }

    public TelegramClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
