package io.github.shahbozolmosov.keyboard.inline;

import java.util.List;

public record InlineKeyboardRow(
        List<InlineKeyboardButton> buttons
) implements InlineKeyboardElement {
}
