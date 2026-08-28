package io.github.solmosov.telegrambot.handler;

import io.github.solmosov.telegrambot.annotation.BotAuthorize;
import io.github.solmosov.telegrambot.context.BotContext;
import io.github.solmosov.telegrambot.model.Update;

import java.util.function.BiConsumer;

public final class Handler {

    private final BiConsumer<Update, BotContext> executor;
    private final BotAuthorize authorization;

    private String callbackPattern = "";

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

    public void setCallbackPattern(String pattern){
        this.callbackPattern = pattern;
    }

    public String getCallbackPattern(){
        return callbackPattern;
    }
}
