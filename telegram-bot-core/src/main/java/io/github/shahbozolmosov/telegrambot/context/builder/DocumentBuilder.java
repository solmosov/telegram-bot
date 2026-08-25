package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.media.SendDocumentRequest;
import io.github.shahbozolmosov.telegrambot.request.message.options.LinkPreviewOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class DocumentBuilder extends AbstractMessageBuilder<Message> {

    private static final Logger log = LoggerFactory.getLogger(DocumentBuilder.class);
    private final SendDocumentRequest.Builder reqBuilder;

    private String chatId;


    public DocumentBuilder(
            TelegramClient client,
            Long updateId,
            String chatId,
            String documentUrl
    ) {
        super(client, updateId);
        this.chatId = chatId;
        this.reqBuilder = SendDocumentRequest.builder()
                .document(documentUrl)
                .chatId(chatId);
    }

    public DocumentBuilder toChat(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public DocumentBuilder options(Consumer<SendDocumentRequest.Builder> consumer){
        consumer.accept(reqBuilder);
        return this;
    }

    public DocumentBuilder keyboard(ReplyMarkup replyMarkup){
        reqBuilder.replyMarkup(replyMarkup);
        return this;
    }

    public DocumentBuilder linkPreviewOptions(Consumer<LinkPreviewOptions.Builder> consumer){
        reqBuilder.linkPreviewOptions(consumer);
        return this;
    }

    public TelegramResponse<Message> send() {
        log.debug("Send to document to updateId: {} chatId: {}", getUpdateId(), chatId);

        SendDocumentRequest request = reqBuilder
                .chatId(chatId)
                .build();

        return client.sendDocument(request);
    }
}
