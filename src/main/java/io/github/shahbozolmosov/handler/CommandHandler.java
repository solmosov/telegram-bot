package io.github.shahbozolmosov.handler;

import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.model.Update;

@FunctionalInterface
public interface CommandHandler {

    void handle(BotContext context);
}
