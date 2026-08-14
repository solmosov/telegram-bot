package io.github.shahbozolmosov.context;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboardMarkup;
import io.github.shahbozolmosov.model.From;
import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.ParseMode;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.request.DeleteMessageRequest;
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
        return String.valueOf(message.chat().id());
    }

    public From from() {
        return message.from();
    }

    public String text() {
        return message.text();
    }

    // --------------------- Send Message ---------------------
    public TelegramResponse<Message> sendText(String text) {
        return sendText(
                this.chatId(),
                text
        );
    }

    public TelegramResponse<Message> sendText(String chatId, String text) {
        return telegramClient.sendMessage(
                chatId,
                text
        );
    }

    public TelegramResponse<Message> sendText(String text, ReplyMarkup replyMarkup) {
        return sendText(
                this.chatId(),
                text,
                replyMarkup
        );
    }

    public TelegramResponse<Message> sendText(String chatId, String text, ReplyMarkup replyMarkup) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId,
                        text,
                        replyMarkup
                )
        );
    }

    // --------------------- Edit message ---------------------
    public TelegramResponse<Message> editText(String text, long messageId) {
        return editText(
                messageId,
                chatId(),
                text
        );
    }

    public TelegramResponse<Message> editText(long messageId, String chatId, String text) {
        return telegramClient.editMessage(
                chatId,
                messageId,
                text
        );
    }

    public TelegramResponse<Message> editText(long messageId, String text, ReplyMarkup replyMarkup) {
        return editText(
                chatId(),
                messageId,
                text,
                replyMarkup
        );
    }

    public TelegramResponse<Message> editText(String chatId, long messageId, String text, ReplyMarkup replyMarkup) {
        return telegramClient.editMessage(
                new EditMessageRequest(
                        chatId,
                        messageId,
                        text,
                        replyMarkup
                )
        );
    }

    // --------------------- Send HTML ---------------------
    public TelegramResponse<Message> sendHtml(String html) {
        return sendHtml(
                chatId(),
                html
        );
    }

    public TelegramResponse<Message> sendHtml(String chatId, String html) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId,
                        html,
                        ParseMode.HTML,
                        null
                )
        );
    }

    public TelegramResponse<Message> sendHtml(String html, ReplyMarkup replyMarkup) {
        return sendHtml(
                chatId(),
                html,
                replyMarkup
        );
    }

    public TelegramResponse<Message> sendHtml(String chatId, String html, ReplyMarkup replyMarkup) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId,
                        html,
                        ParseMode.HTML,
                        replyMarkup
                )
        );
    }

    public TelegramResponse<Message> editHtml(long messageId, String html) {
        return editHtml(
                chatId(),
                messageId,
                html
        );
    }

    public TelegramResponse<Message> editHtml(String chatId, long messageId, String html) {
        return telegramClient.editMessage(
                new EditMessageRequest(
                        chatId,
                        messageId,
                        html,
                        ParseMode.HTML,
                        null
                )
        );
    }

    public TelegramResponse<Message> editHtml(long messageId, String html, ReplyMarkup replyMarkup) {
        return editHtml(
                chatId(),
                messageId,
                html,
                replyMarkup
        );
    }

    public TelegramResponse<Message> editHtml(String chatId, long messageId, String html, ReplyMarkup replyMarkup) {
        return telegramClient.editMessage(
                new EditMessageRequest(
                        chatId,
                        messageId,
                        html,
                        ParseMode.HTML,
                        replyMarkup
                )
        );
    }

    // --------------------- Send Markdown ---------------------
    public TelegramResponse<Message> sendMarkdown(String value) {
        return sendMarkdown(
                chatId(),
                value
        );
    }

    public TelegramResponse<Message> sendMarkdown(String chatId, String value) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId,
                        value,
                        ParseMode.MARKDOWN,
                        null
                )
        );
    }

    public TelegramResponse<Message> sendMarkdown(String value, ReplyMarkup replyMarkup) {
        return sendMarkdown(
                chatId(),
                value,
                replyMarkup
        );
    }

    public TelegramResponse<Message> sendMarkdown(String chatId, String text, ReplyMarkup replyMarkup) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId,
                        text,
                        ParseMode.MARKDOWN,
                        replyMarkup
                )
        );
    }

    public TelegramResponse<Message> editMarkdown(long messageId, String value) {
        return editMarkdown(
                chatId(),
                messageId,
                value
        );
    }

    public TelegramResponse<Message> editMarkdown(String chatId, long messageId, String value) {
        return telegramClient.editMessage(
                new EditMessageRequest(
                        chatId,
                        messageId,
                        value,
                        ParseMode.MARKDOWN,
                        null
                )
        );
    }

    public TelegramResponse<Message> editMarkdown(long messageId, String value, ReplyMarkup replyMarkup) {
        return editMarkdown(
                chatId(),
                messageId,
                value,
                replyMarkup
        );
    }

    public TelegramResponse<Message> editMarkdown(String chatId, long messageId, String value, ReplyMarkup replyMarkup) {
        return telegramClient.editMessage(
                new EditMessageRequest(
                        chatId,
                        messageId,
                        value,
                        ParseMode.MARKDOWN,
                        replyMarkup
                )
        );
    }

    // --------------------- Send Markdown V2 ---------------------
    public TelegramResponse<Message> sendMarkdownV2(String value) {
        return sendMarkdownV2(
                chatId(),
                value
        );
    }

    public TelegramResponse<Message> sendMarkdownV2(String chatId, String text) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId,
                        text,
                        ParseMode.MARKDOWN_V2,
                        null
                )
        );
    }

    public TelegramResponse<Message> sendMarkdownV2(String value, ReplyMarkup replyMarkup) {
        return sendMarkdownV2(
                chatId(),
                value,
                replyMarkup
        );
    }

    public TelegramResponse<Message> sendMarkdownV2(String chatId, String text, ReplyMarkup replyMarkup) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId,
                        text,
                        ParseMode.MARKDOWN_V2,
                        replyMarkup
                )
        );
    }

    public TelegramResponse<Message> editMarkdownV2(long messageId, String value) {
        return editMarkdownV2(
                chatId(),
                messageId,
                value
        );
    }

    public TelegramResponse<Message> editMarkdownV2(String chatId, long messageId, String value) {
        return telegramClient.editMessage(
                new EditMessageRequest(
                        chatId,
                        messageId,
                        value,
                        ParseMode.MARKDOWN_V2,
                        null
                )
        );
    }


    public TelegramResponse<Message> editMarkdownV2(long messageId, String value, ReplyMarkup replyMarkup) {
        return editMarkdownV2(
                chatId(),
                messageId,
                value,
                replyMarkup
        );
    }

    public TelegramResponse<Message> editMarkdownV2(String chatId, long messageId, String value, ReplyMarkup replyMarkup) {
        return telegramClient.editMessage(
                new EditMessageRequest(
                        chatId,
                        messageId,
                        value,
                        ParseMode.MARKDOWN_V2,
                        replyMarkup
                )
        );
    }

    // --------------------- Delete Message ---------------------
    public TelegramResponse<Boolean> deleteMessage(long messageId) {
        return deleteMessage(
                chatId(),
                messageId
        );
    }

    public TelegramResponse<Boolean> deleteMessage(String chatId, long messageId) {
        return telegramClient.deleteMessage(
                new DeleteMessageRequest(
                        chatId,
                        messageId
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

    public TelegramResponse<Message> sendPhoto(
            SendPhotoRequest.Builder builder,
            ReplyMarkup replyMarkup
    ) {
        SendPhotoRequest requestBody = builder
                .chatId(chatId())
                .replyMarkup(replyMarkup)
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
            SendVideoRequest.Builder builder,
            ReplyMarkup replyMarkup
    ) {
        SendVideoRequest requestBody = builder
                .chatId(chatId())
                .replyMarkup(replyMarkup)
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

    public TelegramResponse<Message> sendVideo(
            SendVideoUploadRequest.Builder builder,
            ReplyMarkup replyMarkup
    ) {
        SendVideoUploadRequest requestBody = builder
                .chatId(chatId())
                .replyMarkup(replyMarkup)
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
