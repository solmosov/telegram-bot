package io.github.shahbozolmosov.context;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboardMarkup;
import io.github.shahbozolmosov.model.From;
import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.ParseMode;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.request.EditMessageRequest;
import io.github.shahbozolmosov.request.SendMessageRequest;
import io.github.shahbozolmosov.request.media.*;

public final class MessageContext {

    private final TelegramClient telegramClient;
    private final Message message;

    public MessageContext(
            TelegramClient telegramClient,
            Message message
    ) {
        this.telegramClient = telegramClient;
        this.message = message;
    }

    public Message message() {
        return message;
    }

    public long messageId() {
        return message().messageId();
    }

    public String chatId() {
        return message.chat().id();
    }

    public From from() {
        return message.from();
    }

    public String text() {
        return message.text();
    }

    // --------------------- Send Message ---------------------
    public TelegramResponse<Message> sendText(String text) {
        return telegramClient.sendMessage(
                this.chatId(),
                text
        );
    }

    public TelegramResponse<Message> sendText(String text, ReplyMarkup replyMarkup) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        this.chatId(),
                        text,
                        replyMarkup
                )
        );
    }

    // --------------------- Edit message ---------------------
    public TelegramResponse<Message> editText(String text) {
        return telegramClient.editMessage(
                chatId(),
                messageId(),
                text
        );
    }

    public TelegramResponse<Message> editText(String text, ReplyMarkup replyMarkup) {
        return telegramClient.editMessage(
                new EditMessageRequest(
                        chatId(),
                        messageId(),
                        text,
                        replyMarkup
                )
        );
    }

    // --------------------- Send HTML ---------------------
    public TelegramResponse<Message> sendHtml(String text) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId(),
                        text,
                        ParseMode.HTML,
                        null
                )
        );
    }

    public TelegramResponse<Message> sendHtml(String text, ReplyMarkup replyMarkup) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId(),
                        text,
                        ParseMode.HTML,
                        replyMarkup
                )
        );
    }

    public TelegramResponse<Message> editHtml(String text) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId(),
                        text,
                        ParseMode.HTML,
                        null
                )
        );
    }

    public TelegramResponse<Message> editHtml(String text, ReplyMarkup replyMarkup) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId(),
                        text,
                        ParseMode.HTML,
                        replyMarkup
                )
        );
    }

    // --------------------- Send Markdown ---------------------
    public TelegramResponse<Message> sendMarkdown(String text) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId(),
                        text,
                        ParseMode.MARKDOWN,
                        null
                )
        );
    }

    public TelegramResponse<Message> sendMarkdown(String text, ReplyMarkup replyMarkup) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId(),
                        text,
                        ParseMode.MARKDOWN,
                        replyMarkup
                )
        );
    }

    public TelegramResponse<Message> editMarkdown(String text) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId(),
                        text,
                        ParseMode.MARKDOWN,
                        null
                )
        );
    }

    public TelegramResponse<Message> editMarkdown(String text, ReplyMarkup replyMarkup) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId(),
                        text,
                        ParseMode.MARKDOWN,
                        replyMarkup
                )
        );
    }

    // --------------------- Send Markdown V2 ---------------------
    public TelegramResponse<Message> sendMarkdownV2(String text) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId(),
                        text,
                        ParseMode.MARKDOWN_V2,
                        null
                )
        );
    }

    public TelegramResponse<Message> sendMarkdownV2(String text, ReplyMarkup replyMarkup) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId(),
                        text,
                        ParseMode.MARKDOWN_V2,
                        replyMarkup
                )
        );
    }

    public TelegramResponse<Message> editMarkdownV2(String text) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId(),
                        text,
                        ParseMode.MARKDOWN_V2,
                        null
                )
        );
    }

    public TelegramResponse<Message> editMarkdownV2(String text, ReplyMarkup replyMarkup) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId(),
                        text,
                        ParseMode.MARKDOWN_V2,
                        replyMarkup
                )
        );
    }

    // --------------------- Send Document ---------------------
    public TelegramResponse<Message> sendDocument(SendDocumentRequest.Builder builder) {
        SendDocumentRequest requestBody = builder.chatId(chatId()).build();
        return telegramClient.sendDocument(requestBody);
    }

    public TelegramResponse<Message> sendDocument(SendDocumentUploadRequest.Builder builder) {
        SendDocumentUploadRequest requestBody = builder.chatId(chatId()).build();

        return telegramClient.sendDocument(requestBody);
    }

    public TelegramResponse<Message> sendDocument(SendDocumentRequest.Builder builder, ReplyMarkup replyMarkup) {
        SendDocumentRequest requestBody = builder
                .chatId(chatId())
                .replyMarkup(replyMarkup)
                .build();

        return telegramClient.sendDocument(requestBody);
    }

    public TelegramResponse<Message> sendDocument(SendDocumentUploadRequest.Builder builder, ReplyMarkup replyMarkup) {
        SendDocumentUploadRequest requestBody = builder
                .chatId(chatId())
                .replyMarkup(replyMarkup)
                .build();

        return telegramClient.sendDocument(requestBody);
    }

    // --------------------- Send Photo ---------------------
    public TelegramResponse<Message> sendPhoto(
            SendPhotoRequest.Builder builder
    ) {
        SendPhotoRequest requestBody = builder
                .chatId(chatId())
                .build();
        return telegramClient.sendPhoto(requestBody);
    }

    // --------------------- Send Video ---------------------
    public TelegramResponse<Message> sendVideo(
            SendVideoRequest.Builder builder
    ) {
        SendVideoRequest requestBody = builder
                .chatId(chatId())
                .build();

        return telegramClient.sendVideo(requestBody);
    }

    public TelegramResponse<Message> sendVideo(
            SendVideoUploadRequest.Builder builder
    ) {
        SendVideoUploadRequest requestBody = builder
                .chatId(chatId())
                .build();

        return telegramClient.sendVideo(requestBody);
    }

    // --------------------- Remove Reply Keyboard ---------------------
    public TelegramResponse<Message> removeReplyKeyboard(String text) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId(),
                        text,
                        ReplyKeyboardMarkup.remove()
                )
        );
    }

}
