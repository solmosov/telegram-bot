package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.media.SendDocumentUploadRequest;
import io.github.shahbozolmosov.telegrambot.request.message.media.SendPhotoRequest;
import io.github.shahbozolmosov.telegrambot.request.message.options.LinkPreviewOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class PhotoBuilder extends AbstractMessageBuilder<Message> {

    private static final Logger log = LoggerFactory.getLogger(PhotoBuilder.class);

    private final SendPhotoRequest.Builder reqBuilder;


    public PhotoBuilder(
            TelegramClient client,
            Long updateId,
            String defaultChatId,
            String photoUrl
    ) {
        super(client, updateId);
        this.reqBuilder = SendPhotoRequest.builder()
                .chatId(defaultChatId)
                .photo(photoUrl);
    }

    public PhotoBuilder toChat(String chatId) {
        reqBuilder.chatId(chatId);
        return this;
    }

    public PhotoBuilder caption(String caption){
        reqBuilder.caption(caption);
        return this;
    }

    public PhotoBuilder options(Consumer<SendPhotoRequest.Builder> consumer){
        consumer.accept(reqBuilder);
        return this;
    }

    public PhotoBuilder keyboard(ReplyMarkup replyMarkup){
        reqBuilder.replyMarkup(replyMarkup);
        return this;
    }

    public PhotoBuilder linkPreviewOptions(Consumer<LinkPreviewOptions.Builder> consumer){
        reqBuilder.linkPreviewOptions(consumer);
        return this;
    }

    public TelegramResponse<Message> send() {

        SendPhotoRequest request = reqBuilder.build();

        log.debug("Sending  photo message to updateId: {} chatId: {}", getUpdateId(), request.getChatId());

        return client.sendPhoto(request);
    }
}
