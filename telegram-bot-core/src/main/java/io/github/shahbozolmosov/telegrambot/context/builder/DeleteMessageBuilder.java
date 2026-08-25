package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.message_action.DeleteMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteMessageBuilder {

    private static final Logger log = LoggerFactory.getLogger(DeleteMessageBuilder.class);
    private final TelegramClient client;
    private final Long updateId;

    private String targetChatId;
    private String messageId;

    public DeleteMessageBuilder(
            TelegramClient client,
            Long updateId,
            String chatId,
            String messageId
    ) {
        this.client = client;
        this.updateId = updateId;
        this.targetChatId = chatId;
        this.messageId = messageId;
    }

    public DeleteMessageBuilder toChat(String chatId) {
        this.targetChatId = chatId;
        return this;
    }

    public DeleteMessageBuilder messageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public TelegramResponse<Boolean> send() {
        log.debug("Send delete message to updateId: {} chatId: {} messageId: {}", updateId == null ? "-" : updateId, targetChatId, messageId);

        DeleteMessageRequest request = new DeleteMessageRequest(
                targetChatId,
                messageId
        );

        return client.deleteMessage(request);
    }
}
