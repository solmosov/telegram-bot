package io.github.shahbozolmosov.telegrambot.example.bot;

import io.github.shahbozolmosov.telegrambot.annotation.BotAuthorize;
import io.github.shahbozolmosov.telegrambot.annotation.BotHandler;
import io.github.shahbozolmosov.telegrambot.annotation.CommandHandler;
import io.github.shahbozolmosov.telegrambot.context.BotContext;

@BotHandler("admin")
public class MyAdminBot {

    @BotAuthorize("ADMIN")
    @CommandHandler("/start")
    public void start(BotContext context) {
        context.message().sendText("Welcome to admin bot");
    }
}
