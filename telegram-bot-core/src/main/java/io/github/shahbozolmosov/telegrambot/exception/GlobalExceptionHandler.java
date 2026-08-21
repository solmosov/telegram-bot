package io.github.shahbozolmosov.telegrambot.exception;

import io.github.shahbozolmosov.telegrambot.context.BotContext;
import io.github.shahbozolmosov.telegrambot.model.Update;

public interface GlobalExceptionHandler {

    void handle(Exception exception, Update update, BotContext context);
}
