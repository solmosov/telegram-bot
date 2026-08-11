package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.*;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboardButton;
import io.github.shahbozolmosov.keyboard.reply.RequestUsers;
import io.github.shahbozolmosov.model.Contact;
import io.github.shahbozolmosov.model.Location;

import static io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard.*;

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


    @Message("request users")
    public void requestUsers(BotContext context) {
        var keyboard = ReplyKeyboard.of(
                buttonRequestUsers(
                        "Select users",
                        RequestUsers.user(12345, 7)
                )
        );

        context.message().sendText("Please share users", keyboard);
    }
}
