package io.github.shahbozolmosov.exception.api;

public class TelegramApiException extends RuntimeException {
    private final Integer errorCode;

    public TelegramApiException(Integer errorCode, String description) {
        super(description);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
