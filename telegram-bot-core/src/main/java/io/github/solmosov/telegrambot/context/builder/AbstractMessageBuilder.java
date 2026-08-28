package io.github.solmosov.telegrambot.context.builder;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.model.TelegramResponse;

public abstract class AbstractMessageBuilder<R> {
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
