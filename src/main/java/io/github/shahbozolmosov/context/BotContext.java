package io.github.shahbozolmosov.context;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.keyboard.InlineKeyboardMarkup;
import io.github.shahbozolmosov.model.*;
import io.github.shahbozolmosov.request.EditMessageRequest;
import io.github.shahbozolmosov.request.SendMessageRequest;

import java.util.List;

public final class BotContext {

    private final TelegramClient telegramClient;
    private final Update update;
    private String[] callbackParams;


    private final MessageContext messageContext;
    private final PhotoContext photoContext;
    private final CallbackQueryContext callbackQueryContext;

    public BotContext(
            TelegramClient telegramClient,
            Update update
    ) {
        this.telegramClient = telegramClient;
        this.update = update;
        this.messageContext = update.message() != null
                ? new MessageContext(telegramClient, update.message())
                : null;

        this.photoContext = update.message() != null
                ? new PhotoContext(update.message())
                : null;

        this.callbackQueryContext = update.callbackQuery() != null
                ? new CallbackQueryContext(telegramClient, update.callbackQuery())
                : null;
    }

    // --------------------- Current Update ---------------------
    public Update update() {
        return update;
    }

    // --------------------- Message Context ---------------------
    public MessageContext message() {
        return messageContext;
    }


    // --------------------- Photo Context ---------------------
    public PhotoContext photo() {
        return photoContext;
    }


    // --------------------- Answer Callback Query Context ---------------------
    public CallbackQueryContext callbackQuery() {
        return callbackQueryContext;
    }


    // --------------------- Callback Params ---------------------
    public void setCallbackParams(String[] callbackParams) {
        this.callbackParams = callbackParams;
    }

    public String[] callbackParams() {
        return callbackParams;
    }
}
