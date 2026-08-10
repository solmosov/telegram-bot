package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.annotation.Photo;
import io.github.shahbozolmosov.annotation.Updates;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.InlineKeyboardButton;
import io.github.shahbozolmosov.keyboard.InlineKeyboardMarkup;

import java.util.List;

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
    public void inlineKeyboard(BotContext context){
        var inlineKeyboard = new InlineKeyboardMarkup(
                List.of(
                        List.of(
                                new InlineKeyboardButton("Pizza", "pizza")
                        ),
                        List.of(
                                new InlineKeyboardButton("Burger", "burger")
                        )
                )
        );
        context.sendMessage("Inline Keyboard text", inlineKeyboard);
    }

    @Photo
    public void allSendingPhotos(BotContext context){
        System.out.println("-------------------------photo received");
        context.sendMessage("Photo received: " + context.originalPhoto().fileId());
    }

    public void other() {
        System.out.println("Other method");
    }


    @Updates
    public void onUpdate(BotContext context) {
        System.out.println("Running log: update_id=" + context.update());
    }
}
