package io.github.shahbozolmosov.exception;

import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.model.Update;

public interface GlobalExceptionHandler {

    void handle(Exception exception, Update update, BotContext context);
}
