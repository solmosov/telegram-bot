package io.github.solmosov.telegrambot.exception;

import io.github.solmosov.telegrambot.context.BotContext;
import io.github.solmosov.telegrambot.model.Update;

public interface GlobalExceptionHandler {

    void handle(Exception exception, Update update, BotContext context);
}
