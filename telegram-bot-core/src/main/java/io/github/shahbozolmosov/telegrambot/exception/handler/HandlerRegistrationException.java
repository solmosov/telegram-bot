package io.github.shahbozolmosov.telegrambot.exception.handler;

import io.github.shahbozolmosov.telegrambot.exception.TelegramBotException;

public class HandlerRegistrationException extends TelegramBotException {
    public HandlerRegistrationException(String message) {
        super(message);
    }
}
