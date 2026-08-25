package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.model.InputFile;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.media.SendPhotoRequest;
import io.github.shahbozolmosov.telegrambot.request.message.media.SendPhotoUploadRequest;
import io.github.shahbozolmosov.telegrambot.request.message.options.LinkPreviewOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class PhotoUploadBuilder extends AbstractMessageBuilder<Message> {

    private static final Logger log = LoggerFactory.getLogger(PhotoUploadBuilder.class);

    private final SendPhotoUploadRequest.Builder reqBuilder;

    private String chatId;

    public PhotoUploadBuilder(
            TelegramClient client,
            Long updateId,
            String chatId,
            byte[] file,
            String fileName,
            String mimeType
    ) {
        super(client, updateId);

        InputFile inputFile = new InputFile(file, fileName, mimeType);
        this.reqBuilder = SendPhotoUploadRequest.builder()
                .chatId(chatId)
                .photo(inputFile);
    }

    public PhotoUploadBuilder toChat(String chatId) {
        reqBuilder.chatId(chatId);
        return this;
    }

    public PhotoUploadBuilder options(Consumer<SendPhotoUploadRequest.Builder> consumer){
        consumer.accept(reqBuilder);
        return this;
    }

    public PhotoUploadBuilder keyboard(ReplyMarkup replyMarkup){
        reqBuilder.replyMarkup(replyMarkup);
        return this;
    }

    public PhotoUploadBuilder linkPreviewOptions(Consumer<LinkPreviewOptions.Builder> consumer){
        reqBuilder.linkPreviewOptions(consumer);
        return this;
    }


    public TelegramResponse<Message> send() {
        log.debug("Send upload photo to updateId: {} chatId: {}", updateId == null ? "-" : updateId, chatId);

        SendPhotoUploadRequest request = reqBuilder
                .chatId(chatId)
                .build();


        return client.sendPhoto(request);
    }
}
