package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.*;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.inline.InlineKeyboard;

import static io.github.shahbozolmosov.keyboard.inline.InlineKeyboard.button;

@BotHandler("support")
public class MySupportBot {


    @CommandHandler("/start")
    public void start(BotContext context) {

        var keyboard = InlineKeyboard.of(
                button("Button 1", "button1")
        );

        context.message().sendText("Welcome to support bot", keyboard);
    }


    @BotAuthorize("SUPPORT")
    @MessageHandler("dashboard")
    public void dashboard(BotContext context) {
        context.message().sendText("Support dashboard");
    }

    @CallbackQueryHandler("button1")
    public void button1(BotContext context) {
        var keyboard = InlineKeyboard.removeKeyboard();

        System.out.println("render");

        context.message().removeInlineKeyboard(context.message().messageId(), keyboard);
    }
}
