package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.model.InputFile;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.media.SendDocumentUploadRequest;
import io.github.shahbozolmosov.telegrambot.request.message.options.LinkPreviewOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class DocumentUploadBuilder extends AbstractMessageBuilder<Message> {

    private static final Logger log = LoggerFactory.getLogger(DocumentUploadBuilder.class);

    private final SendDocumentUploadRequest.Builder reqBuilder;


    public DocumentUploadBuilder(
            TelegramClient client,
            Long updateId,
            String defaultChatId,
            byte[] file,
            String fileName,
            String mimeType
    ) {
        super(client, updateId);

        InputFile inputFile = new InputFile(file, fileName, mimeType);

        this.reqBuilder = SendDocumentUploadRequest.builder()
                .chatId(defaultChatId)
                .document(inputFile);
    }

    public DocumentUploadBuilder toChat(String chatId) {
        reqBuilder.chatId(chatId);
        return this;
    }

    public DocumentUploadBuilder options(Consumer<SendDocumentUploadRequest.Builder> consumer){
        consumer.accept(reqBuilder);
        return this;
    }

    public DocumentUploadBuilder keyboard(ReplyMarkup replyMarkup){
        reqBuilder.replyMarkup(replyMarkup);
        return this;
    }

    public DocumentUploadBuilder linkPreviewOptions(Consumer<LinkPreviewOptions.Builder> consumer){
        reqBuilder.linkPreviewOptions(consumer);
        return this;
    }

    public TelegramResponse<Message> send() {
        SendDocumentUploadRequest request = reqBuilder.build();

        log.debug("Send upload document to updateId: {} chatId: {}", getUpdateId(), request.getChatId());

        return client.sendDocument(request);
    }
}
