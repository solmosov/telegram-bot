package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.BotHandler;
import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard;
import io.github.shahbozolmosov.request.media.SendVideoRequest;

import static io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard.button;

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

        var keyboard = ReplyKeyboard.of(
                button("My Photo")
        );

        context.message().sendHtml(html, keyboard);
    }

    @Message("My video")
    public void myVideo(BotContext context) {
        var video = SendVideoRequest.builder()
                .video("https://loremipsum.video/vt/powerpoint-1.mp4")
                .hasSpoiler(true)
                .html("""
                        <b> Lorem ipsum video </b>

                        <i> 1920x1080 @ 30 fps, and 30s long.</i>
                        """);

        context.message().sendVideo(video);
    }

}
