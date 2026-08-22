package io.github.shahbozolmosov.telegrambot.example.bot;

import io.github.shahbozolmosov.telegrambot.annotation.BotAuthorize;
import io.github.shahbozolmosov.telegrambot.annotation.BotHandler;
import io.github.shahbozolmosov.telegrambot.annotation.CommandHandler;
import io.github.shahbozolmosov.telegrambot.annotation.MessageHandler;
import io.github.shahbozolmosov.telegrambot.context.BotContext;
import io.github.shahbozolmosov.telegrambot.keyboard.inline.InlineKeyboard;
import io.github.shahbozolmosov.telegrambot.keyboard.inline.InlineKeyboardButton;

import static io.github.shahbozolmosov.telegrambot.keyboard.inline.InlineKeyboard.*;

@BotHandler("support")
public class MySupportBot {


    @CommandHandler("/start")
    public void start(BotContext context) {

        var keyboard = InlineKeyboard.of(
                row(
                        button("Primary", "primary", InlineKeyboardButton.Style.PRIMARY),
                        button("Success", "success", InlineKeyboardButton.Style.SUCCESS),
                        button("Danger", "danger", InlineKeyboardButton.Style.DANGER),
                        button("Default", "default", InlineKeyboardButton.Style.DEFAULT)
                ),
                buttonUrl("YouTube", "https://youtube.com", InlineKeyboardButton.Style.PRIMARY),
                buttonWebApp("Google", "https://google.com", InlineKeyboardButton.Style.SUCCESS)
        );

        context.message().sendText("Welcome to support bot \n\n Params: " + context.deepLinkParam(), keyboard);
    }

    @CommandHandler("/start@aslkjdlw289higbuyas7891i123_Bot")
    public void startInGroup(BotContext context) {
        context.message().sendText("Hello group users. \n\n Param: " + context.deepLinkParam());
    }


    @BotAuthorize("SUPPORT")
    @MessageHandler("dashboard")
    public void dashboard(BotContext context) {
        context.message().sendText("Support dashboard");
    }
}
