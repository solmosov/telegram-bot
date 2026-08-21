package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.*;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.inline.InlineKeyboard;
import io.github.shahbozolmosov.keyboard.inline.InlineKeyboardButton;

import static io.github.shahbozolmosov.keyboard.inline.InlineKeyboard.button;
import static io.github.shahbozolmosov.keyboard.inline.InlineKeyboard.row;

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
                )
        );

        context.message().sendText("Welcome to support bot", keyboard);
    }


    @BotAuthorize("SUPPORT")
    @MessageHandler("dashboard")
    public void dashboard(BotContext context) {
        context.message().sendText("Support dashboard");
    }
}
