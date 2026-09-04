package io.github.solmosov.telegrambot.context;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.solmosov.telegrambot.messaging.builder.*;
import io.github.solmosov.telegrambot.model.Update;
import io.github.solmosov.telegrambot.request.message.message_action.EditMessageReplyMarkupRequest;

public final class BotContext {

    private final TelegramClient client;
    private final Update update;
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

        if (update.message() != null) {
            this.messageContext = new MessageContext(update.message());
        } else if (update.callbackQuery() != null && update.callbackQuery().message() != null) {
            this.messageContext = new MessageContext(update.callbackQuery().message());
        } else {
            this.messageContext = null;
        }

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

    public long messageId() {
        return message().messageId();
    }

    // --------------------- Requests ---------------------
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

    public EditMessageTextBuilder editMessage(long messageId, String textContent) {
        return new EditMessageTextBuilder(
                client,
                update.updateId(),
                message().chatId(),
                messageId,
                textContent
        );
    }

    public EditMessageCaptionBuilder editMessageCaption(long messageId, String caption) {
        return new EditMessageCaptionBuilder(
                client,
                update.updateId(),
                message().chatId(),
                messageId,
                caption
        );
    }

    public EditMessageReplyMarkupBuilder editInlineKeyboard(long messageId, ReplyMarkup replyMarkup) {
        return new EditMessageReplyMarkupBuilder(
                client,
                update.updateId(),
                message().chatId(),
                messageId,
                replyMarkup
        );
    }

    public EditMessageReplyMarkupBuilder removeInlineKeyboard(long messageId) {
        return new EditMessageReplyMarkupBuilder(
                client,
                update().updateId(),
                message().chatId(),
                messageId
        );
    }

    public DeleteMessageBuilder deleteMessage(long messageId) {
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

    public AudioBuilder audio(String audioUrl) {
        return new AudioBuilder(
                client,
                update.updateId(),
                message().chatId(),
                audioUrl
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


    // --------------------- Callback Query ---------------------
    public CallbackQueryContext callbackQuery() {
        return callbackQueryContext;
    }

    // --------------------- Depp Link Params ---------------------
    public void setDeepLinkParam(String param) {
        deepLinkParams = param;
    }

    public String deepLinkParam() {
        return deepLinkParams;
    }
}
