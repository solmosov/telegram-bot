package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.media.EditMessageCaptionRequest;
import io.github.shahbozolmosov.telegrambot.request.message.options.LinkPreviewOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class EditMessageCaptionBuilder extends AbstractMessageBuilder<Message> {

    private static final Logger log = LoggerFactory.getLogger(EditMessageCaptionBuilder.class);

    private final EditMessageCaptionRequest.Builder reqBuilder;

    public EditMessageCaptionBuilder(
            TelegramClient client,
            Long updateId,
            Long defaultChatId,
            String messageId,
            String caption
    ) {
        super(client, updateId);
        this.reqBuilder = EditMessageCaptionRequest.builder()
                .chatId(defaultChatId)
                .messageId(messageId)
                .caption(caption);
    }

    public EditMessageCaptionBuilder toChat(long chatId){
        reqBuilder.chatId(chatId);
        return this;
    }

    public EditMessageCaptionBuilder options(Consumer<EditMessageCaptionRequest.Builder> consumer){
        consumer.accept(reqBuilder);
        return this;
    }

    public EditMessageCaptionBuilder keyboard(ReplyMarkup replyMarkup){
        reqBuilder.replyMarkup(replyMarkup);
        return this;
    }

    public EditMessageCaptionBuilder linkPreviewOptions(Consumer<LinkPreviewOptions.Builder> consumer){
        reqBuilder.linkPreviewOptions(consumer);
        return this;
    }

    @Override
    public TelegramResponse<Message> send() {
        EditMessageCaptionRequest request = reqBuilder.build();

        log.debug("Sending edit message caption to updateId: {} chatId: {} messageId: {}", getUpdateId(), request.getChatId(), request.getMessageId());

        return client.editMessageCaption(request);
    }
}
