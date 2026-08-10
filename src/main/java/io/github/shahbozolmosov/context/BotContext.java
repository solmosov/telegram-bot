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

    public BotContext(
            TelegramClient client,
            Update update
    ) {
        this.telegramClient = client;
        this.update = update;
        this.messageContext = update.message() != null
                ? new MessageContext(client, update.message())
                : null;
    }

    // --------------------- Current Update ---------------------
    public Update update() {
        return update;
    }

    public MessageContext message() {
        return messageContext;
    }


    // --------------------- Photo ---------------------
    public PhotoSize originalPhoto() {
        List<PhotoSize> sizes = update.message().photo();

        if (sizes == null || sizes.isEmpty()) {
            throw new IllegalStateException(
                    "originalPhoto() called but this update has no photo. "
                            + "Make sure this is only used inside a @Photo handler."
            );
        }

        PhotoSize largest = sizes.getFirst();

        for (PhotoSize size : sizes) {
            if (size.width() * size.height() > largest.width() * largest.height()) {
                largest = size;
            }
        }

        return largest;
    }

    public List<PhotoSize> photoAllSizes() {
        return update.message().photo();
    }

    public String caption() {
        return update.message().caption();
    }

    // --------------------- Answer Callback Query ---------------------
    public TelegramResponse<Boolean> answerCallbackQuery() {
        return telegramClient.answerCallbackQuery(
                update.callbackQuery().id()
        );
    }

    public TelegramResponse<Boolean> answerCallbackQuery(String text) {
        return telegramClient.answerCallbackQuery(
                update.callbackQuery().id(),
                text
        );
    }

    // --------------------- Callback Params ---------------------
    public void setCallbackParams(String[] callbackParams) {
        this.callbackParams = callbackParams;
    }

    public String[] callbackParams() {
        return callbackParams;
    }
}
