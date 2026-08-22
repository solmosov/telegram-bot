package io.shahbozolmosov.telegrambot.spring.boot.example.bot;

import io.github.shahbozolmosov.telegrambot.annotation.BotHandler;
import io.github.shahbozolmosov.telegrambot.annotation.CommandHandler;
import io.github.shahbozolmosov.telegrambot.context.BotContext;
import org.springframework.stereotype.Component;

@Component
@BotHandler("support")
public class MySupportBot {

    @CommandHandler("/start")
    public void start(BotContext context) {
        context.message().sendText("Hello support");
    }
}
