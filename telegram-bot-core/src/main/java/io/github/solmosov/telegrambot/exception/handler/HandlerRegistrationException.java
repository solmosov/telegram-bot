package io.github.solmosov.telegrambot.exception.handler;

import io.github.solmosov.telegrambot.exception.TelegramBotException;

public class HandlerRegistrationException extends TelegramBotException {
    public HandlerRegistrationException(String message) {
        super(message);
    }
}
