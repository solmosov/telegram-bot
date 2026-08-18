package io.github.shahbozolmosov.handler;

import io.github.shahbozolmosov.annotation.BotAuthorize;
import io.github.shahbozolmosov.context.BotContext;

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
