package io.github.shahbozolmosov.exception.handler;

import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.exception.GlobalExceptionHandler;
import io.github.shahbozolmosov.exception.api.TelegramApiException;
import io.github.shahbozolmosov.exception.authorization.AccessDeniedException;
import io.github.shahbozolmosov.exception.client.TelegramClientException;
import io.github.shahbozolmosov.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultGlobalExceptionHandler implements GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultGlobalExceptionHandler.class);

    @Override
    public void handle(Exception exception, Update update, BotContext context) {
        if (exception instanceof TelegramApiException ex) {
            handleApiException(ex);
            return;
        }

        if (exception instanceof TelegramClientException ex) {
            handleClientException(ex, update);
            return;
        }

        if (exception instanceof AccessDeniedException ex) {
            handleAccessDeniedException(ex, update);
            return;
        }

        handleUnexpectedException(exception, update);
    }

    private void handleApiException(TelegramApiException ex) {
        log.error(
                "Handler error for update | errorCode={} | message={}", ex.getErrorCode(), sanitize(ex.getMessage())
        );
    }


    private void handleClientException(TelegramClientException ex, Update update) {
        log.error(
                "Telegram client error | updateId={} | message={}", update.updateId(), sanitize(ex.getMessage())
        );
    }


    private void handleAccessDeniedException(AccessDeniedException ex, Update update) {
        log.error(
                "Access denied | updatedId={} | message={}", update.updateId(), sanitize(ex.getMessage())
        );
    }

    private void handleUnexpectedException(Exception ex, Update update) {
        log.error(
                "Unexpected error  | updateId={} | message={}", update.updateId(), sanitize(ex.getMessage())
        );

        ex.printStackTrace();
    }

    private static String sanitize(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder(value.length());

        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);

            switch (ch) {
                case '\n' -> result.append("\\n");
                case '\r' -> result.append("\\r");
                case '\t' -> result.append("\\t");
                case '\u001B' -> result.append("\\u001B");

                default -> {
                    if (Character.isISOControl(ch)) {
                        result.append(String.format("\\u%04X", (int) ch));
                    } else {
                        result.append(ch);
                    }
                }
            }
        }

        return result.toString();
    }
}
