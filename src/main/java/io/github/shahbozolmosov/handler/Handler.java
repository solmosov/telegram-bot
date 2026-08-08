package io.github.shahbozolmosov.handler;

import io.github.shahbozolmosov.context.BotContext;

@FunctionalInterface
public interface Handler {
    void handle(BotContext context);
}
