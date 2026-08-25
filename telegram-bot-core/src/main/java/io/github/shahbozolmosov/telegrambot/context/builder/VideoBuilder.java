package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.caption.SendVideoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VideoBuilder {

    private static final Logger log = LoggerFactory.getLogger(VideoBuilder.class);
    private final TelegramClient client;
    private final Long updateId;
    private final SendVideoRequest.Builder reqBuilder;


    private String chatId;

    public VideoBuilder(
            TelegramClient client,
            Long updateId,
            String chatId,
            SendVideoRequest.Builder reqBuilder
    ) {
        this.client = client;
        this.updateId = updateId;
        this.chatId = chatId;
        this.reqBuilder = reqBuilder;
    }

    public VideoBuilder toChat(String chatId) {
        this.chatId = chatId;
        return this;
    }


    public TelegramResponse<Message> send() {
        log.debug("Send video to uploadId: {} chatId: {}", updateId == null ? "-" : updateId, chatId);

        SendVideoRequest request = reqBuilder
                .chatId(chatId)
                .build();

        return client.sendVideo(request);
    }
}
