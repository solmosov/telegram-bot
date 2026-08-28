package io.github.solmosov.telegrambot.exception.api;

public class WebhookSetupException extends TelegramApiException {

    public WebhookSetupException(TelegramApiException ex) {
        super(ex.getErrorCode(), ex.getMessage());
    }
}
