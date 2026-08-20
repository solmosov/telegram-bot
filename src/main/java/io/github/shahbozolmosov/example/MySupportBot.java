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
        String html = """
                <b>Welcome, %s! 👋</b>
                
                I'm glad to see you here.
                Choose an option below to get started.
                """
                .formatted(context.message().from().firstName());

        var keyboard = ReplyKeyboard.of(
                button("Orders")
        );

        context.message().sendHtml(html, keyboard);
    }

    @BotAuthorize("ADMIN")
    @MessageHandler("admin")
    public void admin(BotContext context) {
        context.message().sendText("Hello Admin " + context.message().from().firstName());
    }

    @BotAuthorize("COURIER")
    @MessageHandler("courier")
    public void courier(BotContext context) {
        context.message().sendText("Hello Courier " + context.message().from().firstName());
    }
}
