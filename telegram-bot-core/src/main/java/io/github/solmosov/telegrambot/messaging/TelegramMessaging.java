package io.github.solmosov.telegrambot.messaging;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.solmosov.telegrambot.messaging.builder.*;

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

    public PhotoUploadBuilder photo(byte[] file, String fileName, String mimeType){
        return new PhotoUploadBuilder(
                client,
                file,
                fileName,
                mimeType
        );
    }

    public VideoBuilder video(String videoUrl){
        return new VideoBuilder(
                client,
                videoUrl
        );
    }

    public VideoUploadBuilder video(byte[] file, String fileName, String mimeType){
        return new VideoUploadBuilder(
                client,
                file,
                fileName,
                mimeType
        );
    }

    public AudioBuilder audio(String audioUrl) {
        return new AudioBuilder(
                client,
                audioUrl
        );
    }

    public AudioUploadBuilder audio(byte[] file, String fileName, String mimeType) {
        return new AudioUploadBuilder(
                client,
                file,
                fileName,
                mimeType
        );
    }

    public DocumentBuilder document(String documentUrl){
        return new DocumentBuilder(
                client,
                documentUrl
        );
    }

    public DocumentUploadBuilder document(byte[] file, String fileName, String mimeType){
        return new DocumentUploadBuilder(
                client,
                file,
                fileName,
                mimeType
        );
    }
}
