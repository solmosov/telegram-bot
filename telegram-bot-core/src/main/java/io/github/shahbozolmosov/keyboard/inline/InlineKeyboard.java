package io.github.shahbozolmosov.keyboard.inline;

import io.github.shahbozolmosov.keyboard.ReplyMarkup;

import java.util.ArrayList;
import java.util.List;

public final class InlineKeyboard {

    private InlineKeyboard() {
    }

    public static InlineKeyboardButton button(
            String text,
            String callbackData
    ) {
        return button(text, callbackData, null);
    }

    public static InlineKeyboardButton button(
            String text,
            String callbackData,
            InlineKeyboardButton.Style style
    ) {
        return InlineKeyboardButton.callback(text, callbackData, style);
    }

    public static InlineKeyboardButton buttonUrl(
            String text,
            String url
    ) {
        return buttonUrl(text, url, null);
    }

    public static InlineKeyboardButton buttonUrl(
            String text,
            String url,
            InlineKeyboardButton.Style style
    ) {
        return InlineKeyboardButton.url(text, url, style);
    }

    public static InlineKeyboardButton buttonWebApp(
            String text,
            String webAppUrl
    ) {
        return buttonWebApp(text, webAppUrl, null);
    }

    public static InlineKeyboardButton buttonWebApp(
            String text,
            String webAppUrl,
            InlineKeyboardButton.Style style
    ) {
        return InlineKeyboardButton.webApp(text, webAppUrl, style);
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

    public static ReplyMarkup removeKeyboard() {
        return InlineKeyboardMarkup.remove();
    }
}
