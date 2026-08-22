package io.github.shahbozolmosov.telegrambot.example.exception;

import io.github.shahbozolmosov.telegrambot.context.BotContext;
import io.github.shahbozolmosov.telegrambot.exception.GlobalExceptionHandler;
import io.github.shahbozolmosov.telegrambot.exception.authorization.AccessDeniedException;
import io.github.shahbozolmosov.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyGlobalExceptionHandler implements GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(MyGlobalExceptionHandler.class);

    @Override
    public void handle(Exception exception, Update update, BotContext context) {
        if (exception instanceof AccessDeniedException ex) {
            context.message().sendText(ex.getMessage());
        }

        log.error("Unexpected exception updateId={} | message={}", update.updateId(), exception.getMessage());
    }
}
