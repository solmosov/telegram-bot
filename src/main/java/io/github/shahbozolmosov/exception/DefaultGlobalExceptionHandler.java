package io.github.shahbozolmosov.exception;

import io.github.shahbozolmosov.model.Update;

public class DefaultGlobalExceptionHandler implements GlobalExceptionHandler {
    @Override
    public void handle(Throwable exception, Update update) {
        System.err.println("[Telegram Bot] Handler error for update "
                + update.updateId() + ": " + exception.getMessage());

        // TODO: replace with logger dependency. Slf4j
        exception.printStackTrace();
    }
}
