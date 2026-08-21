package io.github.shahbozolmosov.telegrambot.exception;

public class TelegramBotException extends RuntimeException {

    public TelegramBotException(String message) {
        super(message);
    }

    public TelegramBotException(Throwable cause) {
        super(cause);
    }

    public TelegramBotException(String message, Throwable cause) {
        super(message, cause);
    }
}
