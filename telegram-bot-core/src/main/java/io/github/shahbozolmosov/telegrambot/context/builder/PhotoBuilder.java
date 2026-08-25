package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.caption.SendPhotoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhotoBuilder {

    private static final Logger log = LoggerFactory.getLogger(PhotoBuilder.class);

    private final TelegramClient client;
    private final Long updateId;
    private final SendPhotoRequest.Builder reqBuilder;

    private String targetChatId;

    public PhotoBuilder(
            TelegramClient client,
            Long updateId,
            String chatId,
            SendPhotoRequest.Builder reqBuilder
    ) {
        this.client = client;
        this.updateId = updateId;
        this.targetChatId = chatId;
        this.reqBuilder = reqBuilder;
    }

    public PhotoBuilder toChat(String chatId) {
        this.targetChatId = chatId;
        return this;
    }

    public TelegramResponse<Message> send() {
        log.debug("Send photo message to updateId: {} chatId: {}", updateId == null ? "-" : updateId, targetChatId);

        SendPhotoRequest request = reqBuilder.
                chatId(targetChatId)
                .build();

        return client.sendPhoto(request);
    }
}
