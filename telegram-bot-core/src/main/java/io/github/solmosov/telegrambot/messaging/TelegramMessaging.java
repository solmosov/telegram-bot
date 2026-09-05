package io.github.solmosov.telegrambot.messaging;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.solmosov.telegrambot.messaging.builder.*;

import java.nio.file.Path;

public final class TelegramMessaging {


    private final TelegramClient client;

    public TelegramMessaging(TelegramClient client) {
        this.client = client;
    }

    public ChatActionBuilder chatAction(){
        return new ChatActionBuilder(
                client
        );
    }

    public MessageBuilder message(String textContent) {
        return new MessageBuilder(
                client,
                textContent
        );
    }

    public EditMessageTextBuilder editMessage(long messageId, String textContent){
        return new EditMessageTextBuilder(
                client,
                messageId,
                textContent
        );
    }

    public EditMessageCaptionBuilder editMessageCaption(long messageId, String caption){
        return new EditMessageCaptionBuilder(
                client,
                messageId,
                caption
        );
    }

    public EditMessageReplyMarkupBuilder editInlineKeyboard(long messageId, ReplyMarkup replyMarkup){
        return new EditMessageReplyMarkupBuilder(
                client,
                messageId,
                replyMarkup
        );
    }

    public EditMessageReplyMarkupBuilder removeInlineKeyboard(long messageId){
        return new EditMessageReplyMarkupBuilder(
                client,
                messageId
        );
    }

    public DeleteMessageBuilder deleteMessage(long messageId){
        return new DeleteMessageBuilder(
                client,
                messageId
        );
    }

    public PhotoBuilder photo(String photoUrl){
        return new PhotoBuilder(
                client,
                photoUrl
        );
    }

    public PhotoUploadBuilder photo(Path path, String fileName){
        return new PhotoUploadBuilder(
                client,
                path,
                fileName
        );
    }

    public VideoBuilder video(String videoUrl){
        return new VideoBuilder(
                client,
                videoUrl
        );
    }

    public VideoUploadBuilder video(Path path, String fileName){
        return new VideoUploadBuilder(
                client,
                path,
                fileName
        );
    }

    public AudioBuilder audio(String audioUrl) {
        return new AudioBuilder(
                client,
                audioUrl
        );
    }

    public AudioUploadBuilder audio(Path path, String fileName) {
        return new AudioUploadBuilder(
                client,
                path,
                fileName
        );
    }

    public DocumentBuilder document(String documentUrl){
        return new DocumentBuilder(
                client,
                documentUrl
        );
    }

    public DocumentUploadBuilder document(Path path, String fileName){
        return new DocumentUploadBuilder(
                client,
                path,
                fileName
        );
    }
}
