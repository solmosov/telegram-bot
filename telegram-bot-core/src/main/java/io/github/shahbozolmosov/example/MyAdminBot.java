package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.BotAuthorize;
import io.github.shahbozolmosov.annotation.BotHandler;
import io.github.shahbozolmosov.annotation.CommandHandler;
import io.github.shahbozolmosov.context.BotContext;

@BotHandler("admin")
public class MyAdminBot {

    @BotAuthorize("ADMIN")
    @CommandHandler("/start")
    public void start(BotContext context) {
        context.message().sendText("Welcome to admin bot");
    }
}
