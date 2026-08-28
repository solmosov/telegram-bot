package io.github.solmosov.telegrambot.context.builder;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.solmosov.telegrambot.model.InputFile;
import io.github.solmosov.telegrambot.model.Message;
import io.github.solmosov.telegrambot.model.TelegramResponse;
import io.github.solmosov.telegrambot.request.message.media.SendDocumentUploadRequest;
import io.github.solmosov.telegrambot.request.message.options.LinkPreviewOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class DocumentUploadBuilder extends AbstractMessageBuilder<Message> {

    private static final Logger log = LoggerFactory.getLogger(DocumentUploadBuilder.class);

    private final SendDocumentUploadRequest.Builder reqBuilder;


    public DocumentUploadBuilder(
            TelegramClient client,
            Long updateId,
            Long defaultChatId,
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

    public DocumentUploadBuilder toChat(long chatId) {
        reqBuilder.chatId(chatId);
        return this;
    }

    public DocumentUploadBuilder caption(String caption){
        reqBuilder.caption(caption);
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

        log.debug("Sending  upload document to updateId: {} chatId: {}", getUpdateId(), request.getChatId());

        return client.sendDocument(request);
    }
}
