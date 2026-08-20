package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.BotAuthorize;
import io.github.shahbozolmosov.annotation.BotHandler;
import io.github.shahbozolmosov.annotation.CommandHandler;
import io.github.shahbozolmosov.annotation.MessageHandler;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard;

import static io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard.button;

@BotHandler("support")
public class MySupportBot {


    @CommandHandler("/start")
    public void start(BotContext context) {
        context.message().sendText("Welcome to support bot");
    }


    @BotAuthorize("SUPPORT")
    @MessageHandler("dashboard")
    public void dashboard(BotContext context) {
        context.message().sendText("Support dashboard");
    }
}
