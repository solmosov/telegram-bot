package io.github.shahbozolmosov.telegrambot.model;

public record TelegramResponse<T>(
        boolean ok,
        T result,
        Integer errorCode,
        String description
) {

}
