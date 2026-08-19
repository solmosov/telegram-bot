package io.github.shahbozolmosov.exception.handler;

import io.github.shahbozolmosov.model.Update;

public interface GlobalExceptionHandler {

    void handle(Throwable exception, Update update);
}
