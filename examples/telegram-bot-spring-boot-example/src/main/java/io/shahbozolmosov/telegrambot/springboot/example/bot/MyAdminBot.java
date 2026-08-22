package io.shahbozolmosov.telegrambot.springboot.example.bot;

import io.github.shahbozolmosov.telegrambot.annotation.BotAuthorize;
import io.github.shahbozolmosov.telegrambot.annotation.BotHandler;
import io.github.shahbozolmosov.telegrambot.annotation.CommandHandler;
import io.github.shahbozolmosov.telegrambot.context.BotContext;
import org.springframework.stereotype.Component;

@Component
@BotHandler("admin")
public class MyAdminBot {

    @BotAuthorize("ADMIN")
    @CommandHandler("/start")
    public void start(BotContext context) {
        context.message().sendText("Hello admin");
    }
}
