package io.github.shahbozolmosov.telegrambot.spring.boot;

import io.github.shahbozolmosov.telegrambot.bot.TelegramBotConfig;

public record TelegramBotRegistration(
        String botName,
        String token,
        TelegramBotConfig config
) {
    public TelegramBotRegistration(String botName, String token){
        this(botName, token, null);
    }
}
