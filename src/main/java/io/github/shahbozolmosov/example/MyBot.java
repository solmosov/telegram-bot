package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.BotHandler;
import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.inline.InlineKeyboard;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard;
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

        context.message().sendText("Hello inline keyboard", keyboard);
    }


    @Message("reply k")
    public void replyKeyboard(BotContext context) {
        var keyboard = ReplyKeyboard.of(
                ReplyKeyboard.button("Button 1"),
                ReplyKeyboard.row(
                        ReplyKeyboard.button("Button 2"),
                        ReplyKeyboard.button("Button 3")
                )
        );

        context.message().sendText("Hello reply keyboard", keyboard);
    }

    @Message("reply k builder")
    public void replyKeyboardWithBuilder(BotContext context) {
        var keyboard = ReplyKeyboard.builder()
                .resizeKeyboard(false)
                .oneTimeKeyboard(false)
                .of(
                        ReplyKeyboard.button("Button 1"),
                        ReplyKeyboard.row(
                                ReplyKeyboard.button("Button 2"),
                                ReplyKeyboard.button("Button 3")
                        )
                )
                .build();

        var html = """
                <b>Hello reply keyboard with builder</b>
                
                <b>Params</b>
                · resize_keyboard false
                · one_time_keyboard false
                
                
                """;


        context.message().sendHtml(html, keyboard);

    }

    @Message("Button 1")
    public void replyKeyboardButton1Handler(BotContext context) {
        context.message().sendText("Received Button 1");
    }

    @Message("Button 2")
    public void replyKeyboardButton2Handler(BotContext context) {
        context.message().sendText("Received Button 2");
    }

    @Message
    public void handleAnyMessages(BotContext context) {
        context.message().sendText("Listen all messages");
    }
}
