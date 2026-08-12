package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.BotHandler;
import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.inline.InlineKeyboard;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard;
import io.github.shahbozolmosov.media.Document;

import static io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard.button;

@BotHandler
public class MyBot {

    private static final int SELECT_USERS = 1;
    private static final int SELECT_PREMIUM_USERS = 2;

    @Command("/start")
    public void start(BotContext context) {
        String html = """
                <b>Welcome, %s! 👋</b>
                
                I'm glad to see you here.
                Choose an option below to get started.
                """
                .formatted(context.message().from().firstName());

        var keyboard = ReplyKeyboard.of(
                button("📦 My Orders")
        );

        context.message().sendHtml(html, keyboard);
    }

    @Message("📦 My Orders")
    public void myOrders(BotContext context){

        var document = Document
                .url("https://leman.com/wp-content/uploads/2024/03/placeholder-pdf.pdf")
                .caption("Hello caption")
                .protectContent(true)
                .replyMarkup(
                        InlineKeyboard.of(
                                InlineKeyboard.button("Button", "button1")
                        )
                );

        context.message().sendDocument(document);
    }
}
