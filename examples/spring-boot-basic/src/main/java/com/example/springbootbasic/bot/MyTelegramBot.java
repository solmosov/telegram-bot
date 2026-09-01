package com.example.springbootbasic.bot;

import io.github.solmosov.telegrambot.annotation.BotHandler;
import io.github.solmosov.telegrambot.annotation.CommandHandler;
import io.github.solmosov.telegrambot.annotation.MessageHandler;
import io.github.solmosov.telegrambot.context.BotContext;
import org.springframework.stereotype.Component;

@BotHandler("myBot")
@Component
public class MyTelegramBot {
    @CommandHandler("/start")
    public void start(BotContext context) {
        context.reply("👋Hi %s, Welcome to myBot".formatted(context.message().from().firstName()))
                .send();
    }

    @CommandHandler("/help")
    public void help(BotContext context) {
        String html = """
                <b> Help </b>
                
                Available commands
                
                /start - Start the bot
                /help - Show this help message
                """;

        context.reply(html)
                .options(options -> options
                        .html()
                )
                .send();
    }

    @MessageHandler("Hello")
    public void hello(BotContext context){
        context.reply("Hello World")
                .send();
    }
}
