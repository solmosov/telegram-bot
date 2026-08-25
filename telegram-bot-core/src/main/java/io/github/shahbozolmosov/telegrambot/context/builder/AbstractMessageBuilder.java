package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.telegrambot.model.ParseMode;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;

public abstract class AbstractMessageBuilder<T extends AbstractMessageBuilder<T, R>, R> {
    protected final TelegramClient client;
    private final Long updateId;


    public AbstractMessageBuilder(
            TelegramClient client,
            Long updateId
    ) {
        this.client = client;
        this.updateId = updateId;
    }

    public abstract TelegramResponse<R> send();

    protected String getUpdateId() {
        if (updateId == null) {
            return "-";
        }

        return updateId.toString();
    }
}
