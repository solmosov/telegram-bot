package io.github.shahbozolmosov.telegrambot.context;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.model.CallbackQuery;
import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.callback.AnswerCallbackRequest;

import java.util.Map;

public final class CallbackQueryContext {

    private final TelegramClient telegramClient;
    private final CallbackQuery callbackQuery;
    private Map<String, Object> callbackParams;

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

    public void setCallbackParams(Map<String, Object> callbackParams) {
        this.callbackParams = callbackParams;
    }

    public Map<String, Object> callbackParams() {
        return callbackParams;
    }

    public TelegramResponse<Boolean> answerCallbackQuery(String text) {
        return telegramClient.answerCallbackQuery(
                new AnswerCallbackRequest(
                        callbackQuery.id(),
                        text,
                        null
                )
        );
    }

    public TelegramResponse<Boolean> answerCallbackQueryAlert(String text) {
        return telegramClient.answerCallbackQuery(
                new AnswerCallbackRequest(
                        callbackQuery.id(),
                        text,
                        true
                )
        );
    }
}
