package io.github.shahbozolmosov.context;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.keyboard.InlineKeyboardMarkup;
import io.github.shahbozolmosov.model.From;
import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.PhotoSize;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.request.SendMessageRequest;

import java.util.List;

public final class BotContext {

    private final TelegramClient telegramClient;
    private final Update update;

    public BotContext(
            TelegramClient client,
            Update update
    ) {
        this.telegramClient = client;
        this.update = update;
    }

    public Update update() {
        return update;
    }

    public Message message() {
        return update.message();
    }

    public long chatId() {
        return update.message().chat().id();
    }

    public From from() {
        return update.message().from();
    }

    public String text() {
        return update.message().text();
    }

    public void sendMessage(String text) {
        System.out.println("[BotContext] sendMessage: " + update.message().chat().id() + " = " + text);
        telegramClient.sendMessage(
                update.message().chat().id(),
                text
        );
    }

    public void sendMessage(String text, InlineKeyboardMarkup replyMarkup) {
        telegramClient.sendMessage(
                new SendMessageRequest(
                        update.message().chat().id(),
                        text,
                        replyMarkup
                )
        );
    }

    // Photo
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
}
