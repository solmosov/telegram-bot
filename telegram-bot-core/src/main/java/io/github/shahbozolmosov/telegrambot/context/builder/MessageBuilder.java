package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.SendMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageBuilder extends AbstractMessageBuilder<MessageBuilder, Message> {

    private static final Logger log = LoggerFactory.getLogger(MessageBuilder.class);

    private final String textContent;

    public MessageBuilder(
            TelegramClient client,
            Long updateId,
            String defaultChatId,
            String textContent
    ) {
        super(
                client,
                updateId,
                defaultChatId
        );
        this.textContent = textContent;
    }

    @Override
    public TelegramResponse<Message> send() {
        log.debug("Sending message to updateId: {} chatId: {}", getUpdateId(), targetChatId);

        SendMessageRequest request = new SendMessageRequest(
                targetChatId,
                textContent,
                parseMode,
                replyMarkup,
                disableWebPagePreview,
                protectContent,
                disableNotification
        );

        return client.sendMessage(request);
    }
}
