package io.github.solmosov.telegrambot.keyboard.reply;

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
        return new ReplyKeyboardButton(text);
    }

    public static ReplyKeyboardButton buttonLocation(String text) {
        return new ReplyKeyboardButton(text, true);
    }

    public static ReplyKeyboardButton buttonContact(String text) {
        return ReplyKeyboardButton.contact(text);
    }

    public static ReplyKeyboardButton buttonRequestUsers(String text, int requestId, Consumer<RequestUsers.Builder> consumer) {
        RequestUsers.Builder requestUsersBuilder = RequestUsers.builder(requestId);

        consumer.accept(requestUsersBuilder);

        return ReplyKeyboardButton.requestUsers(text, requestUsersBuilder.build());
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
