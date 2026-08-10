package io.github.shahbozolmosov.keyboard;

import java.util.ArrayList;
import java.util.List;

public final class InlineKeyboard {

    private InlineKeyboard() {
    }

    public static InlineKeyboardButton button(
            String text,
            String callbackData
    ) {
        return new InlineKeyboardButton(text, callbackData);
    }

    public static InlineKeyboardRow row(
            InlineKeyboardButton... buttons
    ) {
        return new InlineKeyboardRow(List.of(buttons));
    }

    public static InlineKeyboardMarkup of(
            KeyboardElement... elements
    ) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Object element : elements) {
            if (element instanceof InlineKeyboardButton button) {
                rows.add(List.of(button));
            } else if (element instanceof InlineKeyboardRow(List<InlineKeyboardButton> buttons)) {
                rows.add(buttons);
            }
        }

        return new InlineKeyboardMarkup(rows);
    }
}
