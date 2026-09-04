package io.github.solmosov.telegrambot.messaging.builder;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.solmosov.telegrambot.model.Message;
import io.github.solmosov.telegrambot.model.TelegramResponse;
import io.github.solmosov.telegrambot.request.message.media.SendAudioRequest;
import io.github.solmosov.telegrambot.request.message.options.LinkPreviewOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class AudioBuilder extends AbstractMessageBuilder<Message> {

    private static final Logger log = LoggerFactory.getLogger(AudioBuilder.class);
    private final SendAudioRequest.Builder reqBuilder;

    public AudioBuilder(
            TelegramClient client,
            Long updateId,
            Long defaultChatId,
            String audioUrl
    ) {
        super(client, updateId);
        this.reqBuilder = SendAudioRequest.builder()
                .chatId(defaultChatId)
                .audio(audioUrl);
    }

    public AudioBuilder toChat(long chatId){
        reqBuilder.chatId(chatId);
        return this;
    }

    public AudioBuilder caption(String caption) {
        reqBuilder.caption(caption);
        return this;
    }

    public AudioBuilder options(Consumer<SendAudioRequest.Builder> consumer) {
        consumer.accept(reqBuilder);
        return this;
    }

    public AudioBuilder keyboard(ReplyMarkup replyMarkup) {
        reqBuilder.replyMarkup(replyMarkup);
        return this;
    }

    public AudioBuilder linkPreviewOptions(Consumer<LinkPreviewOptions.Builder> consumer) {
        reqBuilder.linkPreviewOptions(consumer);
        return this;
    }

    @Override
    public TelegramResponse<Message> send() {
        SendAudioRequest request = reqBuilder.build();

        log.debug("Sending audio to updateId: {} chatId: {}", getUpdateId(), request.getChatId());

        return client.sendAudio(request);
    }
}
