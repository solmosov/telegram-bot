package io.github.shahbozolmosov.model;

public record TelegramResponse<T>(
        boolean ok,
        T result,
        Integer errorCode,
        String description
) {

}
