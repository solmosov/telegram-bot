package io.github.shahbozolmosov.keyboard.inline;

import java.util.ArrayList;
import java.util.List;

public final class InlineKeyboard {

    private InlineKeyboard() {
    }

    public static InlineKeyboardButton button(
            String text,
            String callbackData
    ) {
        return new InlineKeyboardButton(text, callbackData, null);
    }

    public static InlineKeyboardButton buttonUrl(
            String text,
            String url
    ) {
        return new InlineKeyboardButton(text, null, url);
    }

    public static InlineKeyboardRow row(
            InlineKeyboardButton... buttons
    ) {
        return new InlineKeyboardRow(List.of(buttons));
    }

    public static InlineKeyboardMarkup of(
            InlineKeyboardElement... elements
    ) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (InlineKeyboardElement element : elements) {
            if (element instanceof InlineKeyboardButton button) {
                rows.add(List.of(button));
            } else if (element instanceof InlineKeyboardRow(List<InlineKeyboardButton> buttons)) {
                rows.add(buttons);
            }
        }

        return new InlineKeyboardMarkup(rows);
    }
}
