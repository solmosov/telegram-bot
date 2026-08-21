package io.github.telegrambot.context;

import io.github.telegrambot.client.TelegramClient;
import io.github.telegrambot.model.Update;

import java.util.Map;

public final class BotContext {

    private final Update update;
    private Map<String, Object> callbackParams;
    private String deepLinkParams;


    private final MessageContext messageContext;
    private final PhotoContext photoContext;
    private final CallbackQueryContext callbackQueryContext;
    private final ReplyKeyboardContext replyKeyboardContext;

    public BotContext(
            TelegramClient telegramClient,
            Update update
    ) {
        this.update = update;
        this.messageContext = update.message() != null
                ? new MessageContext(telegramClient, update.message())
                : update.callbackQuery().message() != null ? new MessageContext(telegramClient, update.callbackQuery().message())
                  : null;

        this.photoContext = update.message() != null
                ? new PhotoContext(update.message())
                : null;

        this.callbackQueryContext = update.callbackQuery() != null
                ? new CallbackQueryContext(telegramClient, update.callbackQuery())
                : null;

        this.replyKeyboardContext = new ReplyKeyboardContext(messageContext);
    }

    // --------------------- Current Update ---------------------
    public Update update() {
        return update;
    }

    // --------------------- Message Context ---------------------
    public MessageContext message() {
        return messageContext;
    }

    // --------------------- Reply Keyboard Context ---------------------
    public ReplyKeyboardContext replyKeyboard() {
        return replyKeyboardContext;
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
    public void setCallbackParams(Map<String, Object> callbackParams) {
        this.callbackParams = callbackParams;
    }

    public Map<String, Object> callbackParams() {
        return callbackParams;
    }

    // --------------------- Depp Link Params ---------------------
    public void setDeepLinkParam(String param){
        deepLinkParams = param;
    }

    public String deepLinkParam(){
        return deepLinkParams;
    }
}
