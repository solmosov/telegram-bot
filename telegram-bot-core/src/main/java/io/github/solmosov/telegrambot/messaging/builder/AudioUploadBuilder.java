package io.github.solmosov.telegrambot.messaging.builder;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.solmosov.telegrambot.model.InputFile;
import io.github.solmosov.telegrambot.model.Message;
import io.github.solmosov.telegrambot.model.TelegramResponse;
import io.github.solmosov.telegrambot.request.message.media.SendAudioUploadRequest;
import io.github.solmosov.telegrambot.request.message.options.LinkPreviewOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

final public  class AudioUploadBuilder extends AbstractMessageBuilder<Message> {

    private static final Logger log = LoggerFactory.getLogger(AudioUploadBuilder.class);
    private final SendAudioUploadRequest.Builder reqBuilder;

    public AudioUploadBuilder(
            TelegramClient client,
            byte[] audio,
            String audioName,
            String mimeType
    ) {
        super(client, null);

        InputFile inputFile = new InputFile(audio, audioName, mimeType);

        this.reqBuilder = SendAudioUploadRequest.builder()
                .audio(inputFile);
    }

    public AudioUploadBuilder(
            TelegramClient client,
            Long updateId,
            Long defaultChatId,
            byte[] audio,
            String audioName,
            String mimeType
    ) {
        super(client, updateId);

        InputFile inputFile = new InputFile(audio, audioName, mimeType);
        this.reqBuilder = SendAudioUploadRequest.builder()
                .chatId(defaultChatId)
                .audio(inputFile);
    }

    public AudioUploadBuilder toChat(long chatId) {
        reqBuilder.chatId(chatId);
        return this;
    }

    public AudioUploadBuilder caption(String caption) {
        reqBuilder.caption(caption);
        return this;
    }

    public AudioUploadBuilder options(Consumer<SendAudioUploadRequest.Builder> consumer) {
        consumer.accept(reqBuilder);
        return this;
    }

    public AudioUploadBuilder keyboard(ReplyMarkup replyMarkup) {
        reqBuilder.replyMarkup(replyMarkup);
        return this;
    }

    public AudioUploadBuilder linkPreviewOptions(Consumer<LinkPreviewOptions.Builder> consumer) {
        reqBuilder.linkPreviewOptions(consumer);
        return this;
    }

    @Override
    public TelegramResponse<Message> send() {
        SendAudioUploadRequest request = reqBuilder.build();

        log.debug("Sending upload audio to updateId: {} chatId: {}", getUpdateId(), request.getChatId());

        return client.sendAudio(request);
    }
}
