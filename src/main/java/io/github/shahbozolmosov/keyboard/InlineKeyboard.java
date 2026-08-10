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

    public static List<InlineKeyboardButton> row(
            InlineKeyboardButton... buttons
    ) {
        return List.of(buttons);
    }

    public static InlineKeyboardMarkup of(
            Object... elements
    ) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Object element : elements) {
            if (element instanceof InlineKeyboardButton button) {
                rows.add(List.of(button));
            } else if (element instanceof List<?> row) {
                rows.add(
                        row.stream()
                                .map(item -> (InlineKeyboardButton) item)
                                .toList()
                );
            }
        }

        return new InlineKeyboardMarkup(rows);
    }
}
