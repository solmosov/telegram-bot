package io.github.telegrambot.exception;

import io.github.telegrambot.context.BotContext;
import io.github.telegrambot.model.Update;

public interface GlobalExceptionHandler {

    void handle(Exception exception, Update update, BotContext context);
}
