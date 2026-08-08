package io.github.shahbozolmosov.exception;

public class TelegramClientException extends RuntimeException {

    public TelegramClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
