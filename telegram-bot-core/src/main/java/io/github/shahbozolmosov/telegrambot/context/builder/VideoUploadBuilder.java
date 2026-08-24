package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.media.send.SendVideoUploadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VideoUploadBuilder {

    private static final Logger log = LoggerFactory.getLogger(VideoUploadBuilder.class);
    private final TelegramClient client;
    private final Long updateId;
    private final SendVideoUploadRequest.Builder reqBuilder;

    private String chatId;

    public VideoUploadBuilder(
            TelegramClient client,
            Long updateId,
            String chatId,
            SendVideoUploadRequest.Builder reqBuilder
    ) {
        this.client = client;
        this.updateId = updateId;
        this.chatId = chatId;
        this.reqBuilder = reqBuilder;
    }

    public VideoUploadBuilder toChat(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public TelegramResponse<Message> send() {
        log.debug("Send upload video to updateId: {} chatId: {}", updateId == null ? "-" : updateId, chatId);

        SendVideoUploadRequest request = reqBuilder
                .chatId(chatId)
                .build();

        return client.sendVideo(request);
    }
}
