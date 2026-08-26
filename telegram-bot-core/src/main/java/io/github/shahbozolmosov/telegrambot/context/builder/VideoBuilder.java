package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.media.SendPhotoUploadRequest;
import io.github.shahbozolmosov.telegrambot.request.message.media.SendVideoRequest;
import io.github.shahbozolmosov.telegrambot.request.message.media.SendVideoUploadRequest;
import io.github.shahbozolmosov.telegrambot.request.message.options.LinkPreviewOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class VideoBuilder extends AbstractMessageBuilder<Message>{

    private static final Logger log = LoggerFactory.getLogger(VideoBuilder.class);

    private final SendVideoRequest.Builder reqBuilder;


    public VideoBuilder(
            TelegramClient client,
            Long updateId,
            String defaultChatId,
            String videoUrl
    ) {
        super(client, updateId);

        this.reqBuilder = SendVideoRequest.builder()
                .chatId(defaultChatId)
                .video(videoUrl);
    }

    public VideoBuilder toChat(String chatId) {
        reqBuilder.chatId(chatId);
        return this;
    }

    public VideoBuilder options(Consumer<SendVideoRequest.Builder> consumer){
        consumer.accept(reqBuilder);
        return this;
    }

    public VideoBuilder keyboard(ReplyMarkup replyMarkup){
        reqBuilder.replyMarkup(replyMarkup);
        return this;
    }

    public VideoBuilder linkPreviewOptions(Consumer<LinkPreviewOptions.Builder> consumer){
        reqBuilder.linkPreviewOptions(consumer);
        return this;
    }


    public TelegramResponse<Message> send() {
        SendVideoRequest request = reqBuilder.build();


        log.debug("Sending  video to uploadId: {} chatId: {}", getUpdateId(), request.getChatId());

        return client.sendVideo(request);
    }
}
