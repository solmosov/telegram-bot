package io.github.shahbozolmosov.telegrambot.context;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.ParseMode;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.SendMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageBuilder {

    private static final Logger log = LoggerFactory.getLogger(MessageBuilder.class);

    private final TelegramClient client;
    private final Long updateId;
    private final String textContent;

    private String targetChatId;
    private ParseMode parseMode;
    private ReplyMarkup replyMarkup;
    private Boolean disableWebPagePreview;

    public MessageBuilder(
            TelegramClient client,
            Long updateId,
            String defaultChatId,
            String textContent
    ) {
        this.client = client;
        this.updateId = updateId;
        this.targetChatId = defaultChatId;
        this.textContent = textContent;
    }

    public MessageBuilder toChat(String chatId) {
        this.targetChatId = chatId;
        return this;
    }

    public MessageBuilder html() {
        this.parseMode = ParseMode.HTML;
        return this;
    }

    public MessageBuilder markdown() {
        this.parseMode = ParseMode.MARKDOWN;
        return this;
    }

    public MessageBuilder markdownV2() {
        this.parseMode = ParseMode.MARKDOWN_V2;
        return this;
    }

    public MessageBuilder replyMarkup(ReplyMarkup replyMarkup) {
        this.replyMarkup = replyMarkup;
        return this;
    }

    public MessageBuilder disableWebPagePreview(Boolean disable) {
        this.disableWebPagePreview = disable;
        return this;
    }

    public TelegramResponse<Message> send() {
        log.debug("Sending message to updateId: {} chatId: {}", updateId == null ? "-" : updateId, targetChatId);

        SendMessageRequest request = new SendMessageRequest(
                targetChatId,
                textContent,
                parseMode,
                replyMarkup,
                disableWebPagePreview
        );

        return client.sendMessage(request);
    }
}
