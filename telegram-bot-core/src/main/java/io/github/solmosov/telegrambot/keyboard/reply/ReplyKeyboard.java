package io.github.solmosov.telegrambot.keyboard.reply;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class ReplyKeyboard {

    private ReplyKeyboard() {
    }

    public static ReplyKeyboardMarkup removeKeyboard() {
        return ReplyKeyboardMarkup.remove();
    }

    public static ReplyKeyboardButton button(String text) {
        return new ReplyKeyboardButton(text);
    }

    public static ReplyKeyboardButton buttonLocation(String text) {
        return new ReplyKeyboardButton(text, true);
    }

    public static ReplyKeyboardButton buttonContact(String text) {
        return  ReplyKeyboardButton.contact(text);
    }

    public static ReplyKeyboardButton buttonRequestUsers(String text, int requestId, Consumer<RequestUsers.Builder> consumer){
        RequestUsers.Builder requestUsersBuilder = RequestUsers.builder(requestId);

        consumer.accept(requestUsersBuilder);

        return ReplyKeyboardButton.requestUsers(text, requestUsersBuilder.build());
    }

    public static ReplyKeyboardRow row(
            ReplyKeyboardButton... buttons
    ) {
        return new ReplyKeyboardRow(List.of(buttons));
    }

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

        return new ReplyKeyboardMarkup(rows);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private boolean resizeKeyboard;
        private boolean oneTimeKeyboard;
        private boolean removeKeyboard;

        private final List<List<ReplyKeyboardButton>> rows = new ArrayList<>();

        public Builder resizeKeyboard(boolean value) {
            this.resizeKeyboard = value;
            return this;
        }

        public Builder oneTimeKeyboard(boolean value) {
            this.oneTimeKeyboard = value;
            return this;
        }

        public Builder removeKeyboard(boolean value) {
            this.removeKeyboard = value;
            return this;
        }

        public Builder of(
                ReplyKeyboardElement... elements
        ) {
            for (ReplyKeyboardElement element : elements) {
                if (element instanceof ReplyKeyboardButton button) {
                    rows.add(List.of(button));
                } else if (element instanceof ReplyKeyboardRow(List<ReplyKeyboardButton> buttons)) {
                    rows.add(buttons);
                }
            }

            return this;
        }

        public ReplyKeyboardMarkup build() {
            return new ReplyKeyboardMarkup(rows, resizeKeyboard, oneTimeKeyboard, removeKeyboard);
        }
    }
}
