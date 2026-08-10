package io.github.shahbozolmosov.context;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.model.CallbackQuery;
import io.github.shahbozolmosov.model.TelegramResponse;

public final class CallbackQueryContext {

    private final TelegramClient telegramClient;
    private final CallbackQuery callbackQuery;

    public CallbackQueryContext(
            TelegramClient telegramClient,
            CallbackQuery callbackQuery
    ) {
        this.telegramClient = telegramClient;
        this.callbackQuery = callbackQuery;
    }

    public TelegramResponse<Boolean> answerCallbackQuery() {
        return telegramClient.answerCallbackQuery(
                callbackQuery.id()
        );
    }

    public TelegramResponse<Boolean> answerCallbackQuery(String text) {
        return telegramClient.answerCallbackQuery(
                callbackQuery.id(),
                text
        );
    }
}
