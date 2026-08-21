package io.github.telegrambot.context;

import io.github.telegrambot.client.TelegramClient;
import io.github.telegrambot.model.CallbackQuery;
import io.github.telegrambot.model.Message;
import io.github.telegrambot.model.TelegramResponse;

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

    public CallbackQuery callbackQuery() {
        return callbackQuery;
    }

    public Message message() {
        return callbackQuery.message();
    }

    public String data() {
        return callbackQuery.data();
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
