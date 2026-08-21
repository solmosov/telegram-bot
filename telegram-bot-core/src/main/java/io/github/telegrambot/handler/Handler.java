package io.github.telegrambot.handler;

import io.github.telegrambot.annotation.BotAuthorize;
import io.github.telegrambot.context.BotContext;

import java.util.function.Consumer;

public final class Handler {

    private final Consumer<BotContext> executor;
    private final BotAuthorize authorization;

    public Handler(
            Consumer<BotContext> executor,
            BotAuthorize authorization
    ) {
        this.executor = executor;
        this.authorization = authorization;
    }

    public void handle(BotContext context) {
        executor.accept(context);
    }

    public BotAuthorize authorization() {
        return authorization;
    }
}
