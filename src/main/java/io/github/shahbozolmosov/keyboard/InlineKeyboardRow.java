package io.github.shahbozolmosov.keyboard;

import java.util.List;

public record InlineKeyboardRow(
        List<InlineKeyboardButton> buttons
) implements KeyboardElement {
}
