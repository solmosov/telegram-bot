package io.github.solmosov.telegrambot.keyboard.reply;

import io.github.solmosov.telegrambot.keyboard.reply.button.RequestUsers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class ReplyKeyboard {

    private ReplyKeyboard() {
    }

    // ---------------- Remove Keyboard ---------------
    public static ReplyKeyboardMarkup removeKeyboard() {
        return removeKeyboard(null);
    }

    public static ReplyKeyboardMarkup removeKeyboard(Boolean selective) {
        return ReplyKeyboardMarkup.remove(selective);
    }

    // ---------------- Buttons ---------------
    public static ReplyKeyboardButton button(String text) {
        return button(text, null);
    }
    public static ReplyKeyboardButton button(String text, ReplyKeyboardButton.Style style) {
        return ReplyKeyboardButton.builder()
                .text(text)
                .style(style)
                .build();
    }

    public static ReplyKeyboardButton buttonLocation(String text) {
        return buttonLocation(text, null);
    }

    public static ReplyKeyboardButton buttonLocation(String text, ReplyKeyboardButton.Style style) {
        return ReplyKeyboardButton.builder()
                .text(text)
                .style(style)
                .requestLocation()
                .build();
    }

    public static ReplyKeyboardButton buttonContact(String text) {
        return buttonContact(text, null);
    }

    public static ReplyKeyboardButton buttonContact(String text, ReplyKeyboardButton.Style style) {
        return ReplyKeyboardButton.builder()
                .text(text)
                .style(style)
                .requestContact()
                .build();
    }

    public static ReplyKeyboardButton buttonRequestUsers(String text, int requestId) {
        RequestUsers.Builder requestUsersBuilder = RequestUsers.builder(requestId);

        return ReplyKeyboardButton.builder()
                .text(text)
                .requestUsers(requestUsersBuilder.build())
                .build();
    }

    public static ReplyKeyboardButton buttonRequestUsers(String text, int requestId, ReplyKeyboardButton.Style style) {
        RequestUsers.Builder requestUsersBuilder = RequestUsers.builder(requestId);

        return ReplyKeyboardButton.builder()
                .text(text)
                .style(style)
                .requestUsers(requestUsersBuilder.build())
                .build();
    }

    public static ReplyKeyboardButton buttonRequestUsers(String text, int requestId, Consumer<RequestUsers.Builder> consumer) {
        return buttonRequestUsers(text, requestId, consumer, null);
    }

    public static ReplyKeyboardButton buttonRequestUsers(String text, int requestId, Consumer<RequestUsers.Builder> consumer, ReplyKeyboardButton.Style style) {
        RequestUsers.Builder requestUsersBuilder = RequestUsers.builder(requestId);

        consumer.accept(requestUsersBuilder);

        return ReplyKeyboardButton.builder()
                .text(text)
                .requestUsers(requestUsersBuilder.build())
                .style(style)
                .build();
    }

    // ---------------- Row ---------------
    public static ReplyKeyboardRow row(
            ReplyKeyboardButton... buttons
    ) {
        return new ReplyKeyboardRow(List.of(buttons));
    }

    // ---------------- Of ---------------
    public static ReplyKeyboardMarkup of(
            ReplyKeyboardElement... elements
    ) {
        List<List<ReplyKeyboardButton>> rows = new ArrayList<>();

        for (ReplyKeyboardElement element : elements) {
            if (element instanceof ReplyKeyboardButton button) {
                rows.add(List.of(button));
            } else if (element instanceof ReplyKeyboardRow(List<ReplyKeyboardButton> buttons)) {
                rows.add(buttons);
            }
        }

        return ReplyKeyboardMarkup.initial(rows);
    }
}
