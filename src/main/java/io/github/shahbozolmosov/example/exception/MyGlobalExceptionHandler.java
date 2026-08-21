package io.github.shahbozolmosov.example.exception;

import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.exception.GlobalExceptionHandler;
import io.github.shahbozolmosov.exception.authorization.AccessDeniedException;
import io.github.shahbozolmosov.model.Update;
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
