package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.model.ParseMode;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;

public abstract class AbstractMessageBuilder<T extends AbstractMessageBuilder<T, R>, R> {
    protected final TelegramClient client;
    private final Long updateId;

    protected String targetChatId;
    protected ParseMode parseMode;
    protected ReplyMarkup replyMarkup;
    protected Boolean disableWebPagePreview;
    protected Boolean protectContent;
    protected Boolean disableNotification;

    public AbstractMessageBuilder(
            TelegramClient client,
            Long updateId,
            String chatId
    ) {
        this.client = client;
        this.updateId = updateId;
        this.targetChatId = chatId;
    }

    @SuppressWarnings("unchecked")
    private T self() {
        return (T) this;
    }

    public T toChat(String chatId) {
        this.targetChatId = chatId;
        return self();
    }

    public T html() {
        this.parseMode = ParseMode.HTML;
        return self();
    }

    public T markdown() {
        this.parseMode = ParseMode.MARKDOWN;
        return self();
    }

    public T markdownV2() {
        this.parseMode = ParseMode.MARKDOWN_V2;
        return self();
    }


    public abstract TelegramResponse<R> send();

    protected String getUpdateId() {
        if (updateId == null) {
            return "-";
        }

        return updateId.toString();
    }
}
