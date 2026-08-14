package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.BotHandler;
import io.github.shahbozolmosov.annotation.CommandHandler;
import io.github.shahbozolmosov.annotation.MessageHandler;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard;

import static io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard.button;

@BotHandler
public class MyBot {

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

    @MessageHandler("hello")
    public void hello(BotContext context) {

        for (int i = 1; i <= 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("Interrupted exception" + ex.getMessage());
            }

            if (i == 3) {
                System.out.println("Business process completed");
            }
        }

        context.message().sendText("Hello " + context.message().from().firstName());
    }

    @MessageHandler("hello2")
    public void hello2(BotContext context){
        context.message().sendText("Hello 2 " + context.message().from().firstName());
    }
}
