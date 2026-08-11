package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.*;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard;
import io.github.shahbozolmosov.model.Contact;
import io.github.shahbozolmosov.model.Location;

import static io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard.buttonContact;

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

    @Message("location")
    public void sendShareLocationBtn(BotContext context) {
        var keyboard = ReplyKeyboard.of(
                ReplyKeyboard.buttonLocation("Share location")
        );

        context.message().sendText("Share your location", keyboard);
    }

    @LocationHandler("Share your location")
    public void handlerLocation(BotContext context) {
        Location location = context.replyKeyboard()
                .location()
                .orElseThrow(() -> {
                    context.message().sendText("Required location");
                    return new IllegalStateException("Required location");
                });

        context.message().sendText("Received your location: [%s, %s]".formatted(location.latitude(), location.longitude()));
    }

    @Message("contact")
    public void sendShareContactBtn(BotContext context) {
        var keyboard = ReplyKeyboard.of(
                buttonContact("📞 Share contact")
        );

        context.message().sendText("Your your contact", keyboard);
    }

    @ContactHandler
    public void contactHandler(BotContext context) {
        Contact contact = context.replyKeyboard().contact();

        context.message().sendText("Received your contact: " + contact.phoneNumber());
    }
}
