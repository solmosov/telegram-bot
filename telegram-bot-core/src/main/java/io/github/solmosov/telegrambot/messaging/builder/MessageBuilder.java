package io.github.solmosov.telegrambot.messaging.builder;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.solmosov.telegrambot.model.Message;
import io.github.solmosov.telegrambot.model.TelegramResponse;
import io.github.solmosov.telegrambot.request.message.options.LinkPreviewOptions;
import io.github.solmosov.telegrambot.request.message.text.SendMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class MessageBuilder extends AbstractMessageBuilder<Message> {

    private static final Logger log = LoggerFactory.getLogger(MessageBuilder.class);

    private final SendMessageRequest.Builder reqBuilder;

    public MessageBuilder(
            TelegramClient client,
            String textContent
    ) {
        super(
                client,
                null
        );
        this.reqBuilder = SendMessageRequest.builder()
                .text(textContent);
    }

    public MessageBuilder(
            TelegramClient client,
            Long updateId,
            Long defaultChatId,
            String textContent
    ) {
        super(
                client,
                updateId
        );

        this.reqBuilder = SendMessageRequest.builder()
                .chatId(defaultChatId)
                .text(textContent);
    }

    public MessageBuilder toChat(long chatId) {
        reqBuilder.chatId(chatId);
        return this;
    }

    public MessageBuilder options(Consumer<SendMessageRequest.Builder> consumer) {
        consumer.accept(reqBuilder);
        return this;
    }

    public MessageBuilder keyboard(ReplyMarkup replyMarkup) {
        reqBuilder.replyMarkup(replyMarkup);
        return this;
    }

    public MessageBuilder linkPreviewOptions(Consumer<LinkPreviewOptions.Builder> consumer) {
        reqBuilder.linkPreviewOptions(consumer);
        return this;
    }

    @Override
    public TelegramResponse<Message> send() {
        SendMessageRequest request = reqBuilder.build();

        log.debug("Sending message to updateId: {} chatId: {}", getUpdateId(), request.getChatId());

        return client.sendMessage(request);
    }
}
