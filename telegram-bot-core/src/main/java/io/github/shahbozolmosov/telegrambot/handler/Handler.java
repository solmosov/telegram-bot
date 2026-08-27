package io.github.shahbozolmosov.telegrambot.handler;

import io.github.shahbozolmosov.telegrambot.annotation.BotAuthorize;
import io.github.shahbozolmosov.telegrambot.context.BotContext;
import io.github.shahbozolmosov.telegrambot.model.Update;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class Handler {

    private final BiConsumer<Update, BotContext> executor;
    private final BotAuthorize authorization;

    public Handler(
            BiConsumer<Update, BotContext> executor,
            BotAuthorize authorization
    ) {
        this.executor = executor;
        this.authorization = authorization;
    }

    public void handle(
            Update update,
            BotContext context
    ) {
        executor.accept(update, context);
    }

    public BotAuthorize authorization() {
        return authorization;
    }
}
