package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.*;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.InlineKeyboard;

import static io.github.shahbozolmosov.keyboard.InlineKeyboard.button;
import static io.github.shahbozolmosov.keyboard.InlineKeyboard.row;

@BotHandler
public class MyBot {
    @Command("/start")
    public void start(BotContext context) {
        context.sendMessage(context.from().firstName() + " welcome, Hello World");
    }

    @Command("/help")
    public void help(BotContext context) {
        context.sendMessage("Help");
    }

    @Message("hello")
    public void hello(BotContext context) {
        context.sendMessage("hello");
    }

    @Message
    public void anyText(BotContext context) {
        System.out.println("-------------------------any hello render");
    }

    @Message("inline keyboard")
    public void inlineKeyboard(BotContext context) {
        var inlineKeyboard = InlineKeyboard.of(
                row(
                        button("Pizza", "pizza"),
                        button("Burger", "burger")
                ),
                button("Btn", "btn")
        );

        context.sendMessage("Inline Keyboard text", inlineKeyboard);
    }

    @CallbackQuery("pizza")
    public void callbackQueryPizza(BotContext context) {
        context.answerCallbackQuery("Pizza is selected");
    }

    @CallbackQuery
    public void watchAllCallback(BotContext context) {
        System.out.println("----------- All Callback Query ------- " + context.update().callbackQuery());
    }

    @Photo
    public void allSendingPhotos(BotContext context) {
        System.out.println("-------------------------photo received");
        context.sendMessage("Photo received: " + context.originalPhoto().fileId());
    }

    public void other() {
        System.out.println("Other method");
    }


    @Updates
    public void onUpdate(BotContext context) {
        System.out.println("Running log: update_id=" + context.update().updateId());
    }
}
