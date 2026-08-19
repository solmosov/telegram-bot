package io.github.shahbozolmosov.exception;

public class DefaultGlobalExceptionHandler implements GlobalExceptionHandler {
    @Override
    public void handle(Throwable exception) {
        System.err.println("[Telegram Bot] Unhandled exception: " + exception.getMessage());

        // TODO: replace with logger dependency. Slf4j
        exception.printStackTrace();
    }
}
