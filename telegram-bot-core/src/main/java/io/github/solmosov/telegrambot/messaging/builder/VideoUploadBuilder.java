package io.github.solmosov.telegrambot.messaging.builder;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.solmosov.telegrambot.model.InputFile;
import io.github.solmosov.telegrambot.model.Message;
import io.github.solmosov.telegrambot.model.TelegramResponse;
import io.github.solmosov.telegrambot.request.message.media.SendVideoUploadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class VideoUploadBuilder extends AbstractMessageBuilder<Message> {

    private static final Logger log = LoggerFactory.getLogger(VideoUploadBuilder.class);

    private final SendVideoUploadRequest.Builder reqBuilder;

    public VideoUploadBuilder(
            TelegramClient client,
            Long updateId,
            Long defaultChatId,
            byte[] file,
            String fileName,
            String mimeType
    ) {
        super(client, updateId);

        InputFile inputFile = new InputFile(file, fileName, mimeType);
        this.reqBuilder = SendVideoUploadRequest.builder()
                .chatId(defaultChatId)
                .video(inputFile);
    }

    public VideoUploadBuilder(
            TelegramClient client,
            byte[] file,
            String fileName,
            String mimeType
    ) {
        super(client, null);

        InputFile inputFile = new InputFile(file, fileName, mimeType);
        this.reqBuilder = SendVideoUploadRequest.builder()
                .video(inputFile);
    }

    public VideoUploadBuilder toChat(long chatId) {
        reqBuilder.chatId(chatId);
        return this;
    }

    public VideoUploadBuilder caption(String caption) {
        reqBuilder.caption(caption);
        return this;
    }

    public VideoUploadBuilder options(Consumer<SendVideoUploadRequest.Builder> consumer) {
        consumer.accept(reqBuilder);
        return this;
    }

    public VideoUploadBuilder keyboard(ReplyMarkup replyMarkup) {
        reqBuilder.replyMarkup(replyMarkup);
        return this;
    }

    public TelegramResponse<Message> send() {

        SendVideoUploadRequest request = reqBuilder.build();

        log.debug("Sending  upload video to updateId: {} chatId: {}", getUpdateId(), request.getChatId());

        return client.sendVideo(request);
    }
}
