package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.caption.SendPhotoUploadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhotoUploadBuilder {

    private static final Logger log = LoggerFactory.getLogger(PhotoUploadBuilder.class);
    private final TelegramClient client;
    private final Long updateId;
    private final SendPhotoUploadRequest.Builder reqBuilder;

    private String chatId;

    public PhotoUploadBuilder(
            TelegramClient client,
            Long updateId,
            String chatId,
            SendPhotoUploadRequest.Builder reqBuilder
    ) {
        this.client = client;
        this.updateId = updateId;
        this.chatId = chatId;
        this.reqBuilder = reqBuilder;
    }

    public PhotoUploadBuilder toChat(String chatId) {
        this.chatId = chatId;
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
