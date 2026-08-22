package io.shahbozolmosov.telegram_bot_spring_boot_example;

import io.github.shahbozolmosov.telegrambot.annotation.BotHandler;
import io.github.shahbozolmosov.telegrambot.annotation.CommandHandler;
import io.github.shahbozolmosov.telegrambot.context.BotContext;
import org.springframework.stereotype.Component;

@Component
@BotHandler("admin")
public class MyAdminBot {

    @CommandHandler("/start")
    public void start(BotContext context) {
        context.message().sendText("Hello admin");
    }
}
