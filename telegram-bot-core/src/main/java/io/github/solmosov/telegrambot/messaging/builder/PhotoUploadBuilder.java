package io.github.solmosov.telegrambot.messaging.builder;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.solmosov.telegrambot.model.InputFile;
import io.github.solmosov.telegrambot.model.Message;
import io.github.solmosov.telegrambot.model.TelegramResponse;
import io.github.solmosov.telegrambot.request.message.media.SendPhotoUploadRequest;
import io.github.solmosov.telegrambot.request.message.options.LinkPreviewOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.function.Consumer;

public class PhotoUploadBuilder extends AbstractMessageBuilder<Message> {

    private static final Logger log = LoggerFactory.getLogger(PhotoUploadBuilder.class);

    private final SendPhotoUploadRequest.Builder reqBuilder;


    public PhotoUploadBuilder(
            TelegramClient client,
            Long updateId,
            Long defaultChatId,
            Path path,
            String fileName
    ) {
        super(client, updateId);

        InputFile inputFile = new InputFile(path, fileName);
        this.reqBuilder = SendPhotoUploadRequest.builder()
                .chatId(defaultChatId)
                .photo(inputFile);
    }

    public PhotoUploadBuilder(
            TelegramClient client,
            Path path,
            String fileName
    ) {
        super(client, null);

        InputFile inputFile = new InputFile(path, fileName);
        this.reqBuilder = SendPhotoUploadRequest.builder()
                .photo(inputFile);
    }

    public PhotoUploadBuilder toChat(long chatId) {
        reqBuilder.chatId(chatId);
        return this;
    }

    public PhotoUploadBuilder caption(String caption) {
        reqBuilder.caption(caption);
        return this;
    }

    public PhotoUploadBuilder options(Consumer<SendPhotoUploadRequest.Builder> consumer) {
        consumer.accept(reqBuilder);
        return this;
    }

    public PhotoUploadBuilder keyboard(ReplyMarkup replyMarkup) {
        reqBuilder.replyMarkup(replyMarkup);
        return this;
    }

    public PhotoUploadBuilder linkPreviewOptions(Consumer<LinkPreviewOptions.Builder> consumer) {
        reqBuilder.linkPreviewOptions(consumer);
        return this;
    }


    public TelegramResponse<Message> send() {
        SendPhotoUploadRequest request = reqBuilder.build();

        log.debug("Sending  upload photo to updateId: {} chatId: {}", getUpdateId(), request.getChatId());

        return client.sendPhoto(request);
    }
}
