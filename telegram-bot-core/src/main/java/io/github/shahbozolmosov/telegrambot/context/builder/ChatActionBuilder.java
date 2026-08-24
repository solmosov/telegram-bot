package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.chatAction.SendChatActionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatActionBuilder {

    private static final Logger log = LoggerFactory.getLogger(ChatActionBuilder.class);
    private final TelegramClient client;
    private final Long updateId;
    private final SendChatActionRequest.Action action;

    private String chatId;

    public ChatActionBuilder(
            TelegramClient client,
            Long updateId,
            String chatId,
            SendChatActionRequest.Action action
    ) {
        this.client = client;
        this.updateId = updateId;
        this.chatId = chatId;
        this.action = action;
    }

    private ChatActionBuilder toChat(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public TelegramResponse<Boolean> send() {
        log.debug("Send chat action to updateId: {} chatId: {}", updateId == null ? "-" : updateId, chatId);

        SendChatActionRequest request = new SendChatActionRequest(
                chatId,
                action
        );

        return client.sendChatAction(request);
    }
}
