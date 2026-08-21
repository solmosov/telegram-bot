package io.github.shahbozolmosov.telegrambot.keyboard.reply;

import java.util.List;

public record ReplyKeyboardRow(
        List<ReplyKeyboardButton> buttons
)implements ReplyKeyboardElement {
}
