package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.AbstractRequest;
import io.github.shahbozolmosov.telegrambot.request.message.media.SendDocumentRequest;
import io.github.shahbozolmosov.telegrambot.request.message.media.SendDocumentUploadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentUploadBuilder extends AbstractMessageBuilder<Message> {

    private static final Logger log = LoggerFactory.getLogger(DocumentUploadBuilder.class);

    private final SendDocumentUploadRequest.Builder reqBuilder;


    public DocumentUploadBuilder(
            TelegramClient client,
            Long updateId,
            String defaultChatId
    ) {
        super(client, updateId);

        this.reqBuilder = SendDocumentUploadRequest.builder()
                .chatId(defaultChatId);
    }

    public DocumentUploadBuilder toChat(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public TelegramResponse<Message> send() {
        log.debug("Send upload document to updateId: {} chatId: {}", updateId == null ? "-" : updateId, chatId);

        SendDocumentUploadRequest request = reqBuilder
                .chatId(chatId)
                .build();

        return client.sendDocument(request);
    }
}
