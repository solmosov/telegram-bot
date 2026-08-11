package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.BotHandler;
import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.inline.InlineKeyboard;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboardButton;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboardMarkup;

import java.util.List;

import static io.github.shahbozolmosov.keyboard.inline.InlineKeyboard.button;
import static io.github.shahbozolmosov.keyboard.inline.InlineKeyboard.row;

@BotHandler
public class MyBot {

    @Command("/start")
    public void start(BotContext context) {
        String html = """
                <b>Welcome, %s! 👋</b>
                
                I'm glad to see you here.
                Choose an option below to get started.
                """
                .formatted(context.message().from().firstName());

        context.message().sendHtml(html);
    }

    @Message("inline k")
    public void inlineKeyboard(BotContext context) {
        var keyboard = InlineKeyboard.of(
                button("Button 1", "button1"),
                row(
                        button("Button 2", "button2"),
                        button("Button 3", "button3")
                )
        );

        context.message().sendText("Hello", keyboard);
    }


    @Message("reply k")
    public void replyKeyboard(BotContext context) {
        var keyboard = new ReplyKeyboardMarkup(
                List.of(
                        List.of(
                                new ReplyKeyboardButton("Button 1")
                        ),
                        List.of(
                                new ReplyKeyboardButton("Button 1"),
                                new ReplyKeyboardButton("Button 1")
                        )
                )
        );

        context.message().sendText("Hello", keyboard);
    }
}
