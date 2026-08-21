package io.github.shahbozolmosov.telegrambot.keyboard.inline;

import java.util.List;

public record InlineKeyboardRow(
        List<InlineKeyboardButton> buttons
) implements InlineKeyboardElement {
}
