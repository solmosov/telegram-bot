package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.message_action.DeleteMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteMessageBuilder extends AbstractMessageBuilder<Boolean> {

    private static final Logger log = LoggerFactory.getLogger(DeleteMessageBuilder.class);

    private String chatId;
    private String messageId;

    public DeleteMessageBuilder(
            TelegramClient client,
            Long updateId,
            String defaultChatId,
            String messageId
    ) {
        super(client, updateId);
        this.chatId = defaultChatId;
        this.messageId = messageId;
    }

    public DeleteMessageBuilder toChat(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public DeleteMessageBuilder messageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public TelegramResponse<Boolean> send() {
        log.debug("Sending  delete message to updateId: {} chatId: {} messageId: {}", getUpdateId(), chatId, messageId);

        DeleteMessageRequest request = new DeleteMessageRequest(
                chatId,
                messageId
        );

        return client.deleteMessage(request);
    }
}
