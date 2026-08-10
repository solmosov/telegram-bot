package io.github.shahbozolmosov.context;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.keyboard.inline.InlineKeyboardMarkup;
import io.github.shahbozolmosov.model.From;
import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.ParseMode;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.request.EditMessageRequest;
import io.github.shahbozolmosov.request.SendMessageRequest;

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

    public long chatId() {
        return message.chat().id();
    }

    public From from() {
        return message.from();
    }

    public String text() {
        return message.text();
    }

    // --------------------- Send Message ---------------------
    public TelegramResponse<Message> sendMessage(String text) {
        return telegramClient.sendMessage(
                this.chatId(),
                text
        );
    }

    public TelegramResponse<Message> sendMessage(String text, InlineKeyboardMarkup replyMarkup) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        this.chatId(),
                        text,
                        replyMarkup
                )
        );
    }

    // --------------------- Edit message ---------------------
    public TelegramResponse<Message> editMessage(String text) {
        return telegramClient.editMessage(
                chatId(),
                messageId(),
                text
        );
    }

    public TelegramResponse<Message> editMessage(String text, InlineKeyboardMarkup replyMarkup) {
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

    public TelegramResponse<Message> sendHtml(String text, InlineKeyboardMarkup replyMarkup) {
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

    public TelegramResponse<Message> editHtml(String text, InlineKeyboardMarkup replyMarkup) {
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

    public TelegramResponse<Message> sendMarkdown(String text, InlineKeyboardMarkup replyMarkup) {
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

    public TelegramResponse<Message> editMarkdown(String text, InlineKeyboardMarkup replyMarkup) {
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

    public TelegramResponse<Message> sendMarkdownV2(String text, InlineKeyboardMarkup replyMarkup) {
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

    public TelegramResponse<Message> editMarkdownV2(String text, InlineKeyboardMarkup replyMarkup) {
        return telegramClient.sendMessage(
                new SendMessageRequest(
                        chatId(),
                        text,
                        ParseMode.MARKDOWN_V2,
                        replyMarkup
                )
        );
    }


}
