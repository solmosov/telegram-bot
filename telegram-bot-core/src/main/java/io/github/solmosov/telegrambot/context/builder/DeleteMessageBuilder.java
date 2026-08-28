package io.github.solmosov.telegrambot.context.builder;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.model.TelegramResponse;
import io.github.solmosov.telegrambot.request.message.message_action.DeleteMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteMessageBuilder extends AbstractMessageBuilder<Boolean> {

    private static final Logger log = LoggerFactory.getLogger(DeleteMessageBuilder.class);

    private long chatId;
    private long messageId;

    public DeleteMessageBuilder(
            TelegramClient client,
            Long updateId,
            Long defaultChatId,
            Long messageId
    ) {
        super(client, updateId);
        this.chatId = defaultChatId;
        this.messageId = messageId;
    }

    public DeleteMessageBuilder toChat(long chatId) {
        this.chatId = chatId;
        return this;
    }

    public DeleteMessageBuilder messageId(long messageId) {
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
