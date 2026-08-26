package io.github.shahbozolmosov.telegrambot.context;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.context.builder.*;
import io.github.shahbozolmosov.telegrambot.model.Update;
import io.github.shahbozolmosov.telegrambot.request.message.media.*;

import java.util.Map;

public final class BotContext {

    private final TelegramClient client;
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
        this.client = telegramClient;
        this.update = update;
        this.messageContext = update.message() != null
                ? new MessageContext(update.message())
                : update.callbackQuery().message() != null ? new MessageContext(update.callbackQuery().message())
                  : null;

        this.photoContext = update.message() != null
                ? new PhotoContext(update.message())
                : null;

        this.callbackQueryContext = update.callbackQuery() != null
                ? new CallbackQueryContext(client, update.callbackQuery())
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

    public String messageId() {
        return message().messageId();
    }

    public ChatActionBuilder chatAction() {
        return new ChatActionBuilder(
                client,
                update.updateId(),
                message().chatId()
        );
    }

    public MessageBuilder reply(String textContext) {
        return new MessageBuilder(
                client,
                update.updateId(),
                message().chatId(),
                textContext
        );
    }

    public EditMessageTextBuilder editMessage(String textContent) {
        return new EditMessageTextBuilder(
                client,
                update.updateId(),
                message().chatId(),
                messageId(),
                textContent
        );
    }

    public DeleteMessageBuilder deleteMessage(String messageId) {
        return new DeleteMessageBuilder(
                client,
                update.updateId(),
                message().chatId(),
                messageId
        );
    }

    public PhotoBuilder photo(String photoUrl) {
        return new PhotoBuilder(
                client,
                update.updateId(),
                message().chatId(),
                photoUrl
        );
    }

    public PhotoUploadBuilder photo(byte[] file, String fileName, String mimeType) {
        return new PhotoUploadBuilder(
                client,
                update.updateId(),
                message().chatId(),
                file,
                fileName,
                mimeType
        );
    }

    public VideoBuilder video(String videoUrl) {
        return new VideoBuilder(
                client,
                update().updateId(),
                message().chatId(),
                videoUrl
        );
    }

    public VideoUploadBuilder video(byte[] file, String fileName, String mimeType) {
        return new VideoUploadBuilder(
                client,
                update().updateId(),
                message().chatId(),
                file,
                fileName,
                mimeType
        );
    }

    public DocumentBuilder document(String documentUrl) {
        return new DocumentBuilder(
                client,
                update.updateId(),
                message().chatId(),
                documentUrl
        );
    }

    public DocumentUploadBuilder document(byte[] file, String fileName, String mimeType) {
        return new DocumentUploadBuilder(
                client,
                update.updateId(),
                message().chatId(),
                file,
                fileName,
                mimeType
        );
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
    public void setDeepLinkParam(String param) {
        deepLinkParams = param;
    }

    public String deepLinkParam() {
        return deepLinkParams;
    }
}
