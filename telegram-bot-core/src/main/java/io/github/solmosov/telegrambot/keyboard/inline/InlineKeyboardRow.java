package io.github.solmosov.telegrambot.keyboard.inline;

import java.util.List;

public record InlineKeyboardRow(
        List<InlineKeyboardButton> buttons
) implements InlineKeyboardElement {
}
