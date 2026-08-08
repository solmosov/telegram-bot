package io.github.shahbozolmosov.handler;

import io.github.shahbozolmosov.model.Update;

@FunctionalInterface
public interface CommandHandler {

    void handle(Update update);
}
