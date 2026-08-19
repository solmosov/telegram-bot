package io.github.shahbozolmosov.exception.handler;

import io.github.shahbozolmosov.exception.GlobalExceptionHandler;
import io.github.shahbozolmosov.exception.api.TelegramApiException;
import io.github.shahbozolmosov.exception.authorization.AccessDeniedException;
import io.github.shahbozolmosov.exception.client.TelegramClientException;
import io.github.shahbozolmosov.model.Update;

// TODO change serr log message replace with Slf4j dependency
public class DefaultGlobalExceptionHandler implements GlobalExceptionHandler {
    @Override
    public void handle(Exception exception, Update update) {
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
        System.err.println(
                "[Telegram Bot] Handler error for update "
                        + " | errorCode=" + ex.getErrorCode()
                        + " | message=" + ex.getMessage()
        );
    }


    private void handleClientException(TelegramClientException ex, Update update) {
        System.err.println(
                "[Telegram Bot] Telegram client error"
                        + " | updateId=" + update.updateId()
                        + " | message=" + update.message()
        );
    }


    private void handleAccessDeniedException(AccessDeniedException ex, Update update) {
        System.err.println(
                "[Telegram Bot] Access denied"
                        + " | updatedId=" + update.updateId()
                        + " | message=" + ex.getMessage()
        );
    }

    private void handleUnexpectedException(Exception exception, Update update) {
        System.err.println(
                "[Telegram Bot] Unexpected error"
                        + " | updateId=" + update.updateId()
                        + " | message=" + exception.getMessage()
        );

        exception.printStackTrace();
    }

}
