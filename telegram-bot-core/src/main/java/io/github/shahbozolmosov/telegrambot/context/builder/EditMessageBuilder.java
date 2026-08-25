package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.ParseMode;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.message.text.EditMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditMessageBuilder {
    private final static Logger log = LoggerFactory.getLogger(EditMessageBuilder.class);

    private final TelegramClient client;
    private final Long updateId;
    private final String messageId;
    private final String textContent;


    private String targetChatId;
    private ParseMode parseMode;
    private ReplyMarkup replyMarkup;
    private Boolean disableWebPagePreview;

    public EditMessageBuilder(
            TelegramClient client,
            Long updateId,
            String chatId,
            String messageId,
            String textContent
    ) {
        this.client = client;
        this.updateId = updateId;
        this.targetChatId = chatId;
        this.messageId = messageId;
        this.textContent = textContent;
    }

    public EditMessageBuilder toChat(String chatId) {
        this.targetChatId = chatId;
        return this;
    }

    public EditMessageBuilder html() {
        this.parseMode = ParseMode.HTML;
        return this;
    }

    public EditMessageBuilder markdown() {
        this.parseMode = ParseMode.MARKDOWN;
        return this;
    }

    public EditMessageBuilder markdownV2() {
        this.parseMode = ParseMode.MARKDOWN_V2;
        return this;
    }

    public EditMessageBuilder replyMarkup(ReplyMarkup replyMarkup) {
        this.replyMarkup = replyMarkup;
        return this;
    }

    public EditMessageBuilder disableWebPagePreview(Boolean disable) {
        this.disableWebPagePreview = disable;
        return this;
    }

    public TelegramResponse<Message> send() {
        log.debug("Sending edit message to updateId: {} chatId: {}", updateId == null ? "-" : updateId, targetChatId);

        EditMessageRequest request = new EditMessageRequest(
                targetChatId,
                messageId,
                textContent,
                parseMode,
                replyMarkup,
                disableWebPagePreview
        );

        return client.editMessage(request);
    }
}
