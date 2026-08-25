package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.caption.SendDocumentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentBuilder {

    private static final Logger log = LoggerFactory.getLogger(DocumentBuilder.class);
    private final TelegramClient client;
    private final Long updateId;
    private final SendDocumentRequest.Builder reqBuilder;

    private String chatId;


    public DocumentBuilder(
            TelegramClient client,
            Long updateId,
            String chatId,
            SendDocumentRequest.Builder reqBuilder
    ) {
        this.client = client;
        this.updateId = updateId;
        this.chatId = chatId;
        this.reqBuilder = reqBuilder;
    }

    public DocumentBuilder toChat(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public TelegramResponse<Message> send() {
        log.debug("Send to document to updateId: {} chatId: {}", updateId == null ? "-" : updateId, chatId);

        SendDocumentRequest request = reqBuilder
                .chatId(chatId)
                .build();

        return client.sendDocument(request);
    }
}
