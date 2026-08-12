package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.BotHandler;
import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard;
import io.github.shahbozolmosov.media.Photo;

import static io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard.button;

@BotHandler
public class MyBot {

    private static final int SELECT_USERS = 1;
    private static final int SELECT_PREMIUM_USERS = 2;
//    private static final byte[] MOCK_PDF =;

    @Command("/start")
    public void start(BotContext context) {
        String html = """
                <b>Welcome, %s! 👋</b>
                
                I'm glad to see you here.
                Choose an option below to get started.
                """
                .formatted(context.message().from().firstName());

        var keyboard = ReplyKeyboard.of(
                button("My Photo")
        );

        context.message().sendHtml(html, keyboard);
    }

    @Message("My Photo")
    public void myPhoto(BotContext context) {
        var photo = Photo
                .photo("https://placehold.co/600x400.png")
                .caption("Photo 600x400")
                .hasSpoiler(true);

        context.message().sendPhoto(photo);
    }

}
