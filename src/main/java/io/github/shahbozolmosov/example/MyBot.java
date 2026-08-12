package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.BotHandler;
import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.inline.InlineKeyboard;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard;
import io.github.shahbozolmosov.media.Video;
import io.github.shahbozolmosov.model.InputFIle;
import io.github.shahbozolmosov.request.media.SendVideoRequest;
import io.github.shahbozolmosov.request.media.SendVideoUploadRequest;

import java.io.IOException;
import java.io.InputStream;

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
                button("My Video"),
                button("My Local Video")
        );

        context.message().sendHtml(html, keyboard);
    }

    @Message("My Video")
    public void myVideo(BotContext context) {
        var video = Video
                .video("https://loremipsum.video/vt/powerpoint-1.mp4")
                .hasSpoiler(true)
                .html("""
                        <b> Lorem ipsum video </b>
                        
                        <i> 1920x1080 @ 30 fps, and 30s long.</i>
                        """);

        var keyboard = InlineKeyboard.of(
                InlineKeyboard.row(
                        InlineKeyboard.button("Button 1", "button1"),
                        InlineKeyboard.button("Button 2", "button2")
                ),
                InlineKeyboard.button("Button 3", "button3")
        );

        context.message().sendVideo(video, keyboard);
    }

    @Message("My Local Video")
    public void myLocalVideo(BotContext context) {
        var video = Video
                .video(getMockFile("/files/video.mp4"), "1080p video", "application/mp4")
                .hasSpoiler(true)
                .caption("Mock video");

        var keyboard = InlineKeyboard.of(
                InlineKeyboard.row(
                        InlineKeyboard.button("Button 1", "button1"),
                        InlineKeyboard.button("Button 2", "button2"),
                        InlineKeyboard.button("Button 3", "button3")
                ),
                InlineKeyboard.button("Button 4", "button4")
        );

        context.message().sendVideo(video, keyboard);

    }

    private byte[] getMockFile(String path) {
        byte[] pdfBytes;
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalStateException("PDF is not found: " + path);
            }
            pdfBytes = is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return pdfBytes;
    }

}
