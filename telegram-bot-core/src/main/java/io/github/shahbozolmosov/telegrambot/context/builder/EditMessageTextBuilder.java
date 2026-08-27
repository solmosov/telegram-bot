package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.options.LinkPreviewOptions;
import io.github.shahbozolmosov.telegrambot.request.message.text.EditMessageTextRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class EditMessageTextBuilder extends AbstractMessageBuilder<Message> {

    private final static Logger log = LoggerFactory.getLogger(EditMessageTextBuilder.class);

    private final EditMessageTextRequest.Builder reqBuilder;

    public EditMessageTextBuilder(
            TelegramClient client,
            Long updateId,
            Long defaultChatId,
            String messageId,
            String textContent
    ) {
        super(client, updateId);
        reqBuilder = EditMessageTextRequest.builder()
                .chatId(defaultChatId)
                .text(textContent)
                .messageId(messageId);
    }

    public EditMessageTextBuilder chatId(long chatId){
        reqBuilder.chatId(chatId);
        return this;
    }

    public EditMessageTextBuilder options(Consumer<EditMessageTextRequest.Builder> consumer) {
        consumer.accept(reqBuilder);
        return this;
    }

    public EditMessageTextBuilder keyboard(ReplyMarkup replyMarkup) {
        reqBuilder.replyMarkup(replyMarkup);
        return this;
    }

    public EditMessageTextBuilder linkPreviewOptions(Consumer<LinkPreviewOptions.Builder> consumer) {
        reqBuilder.linkPreviewOptions(consumer);
        return this;
    }

    @Override
    public TelegramResponse<Message> send() {
        EditMessageTextRequest request = reqBuilder.build();

        log.debug("Sending edit message to updateId: {} chatId: {} messageId: {}", getUpdateId(), request.getChatId(), request.getMessageId());

        return client.editMessage(request);
    }
}
