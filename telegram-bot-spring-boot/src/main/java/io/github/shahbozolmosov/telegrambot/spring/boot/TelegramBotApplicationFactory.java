package io.github.shahbozolmosov.telegrambot.spring.boot;

import io.github.shahbozolmosov.telegrambot.bot.HandlerRegistrationMode;
import io.github.shahbozolmosov.telegrambot.bot.TelegramBotApplication;
import io.github.shahbozolmosov.telegrambot.bot.TelegramBotConfig;

public class TelegramBotApplicationFactory {

    private final TelegramBotProperties properties;

    public TelegramBotApplicationFactory(TelegramBotProperties properties) {
        this.properties = properties;
    }

    public TelegramBotApplication create() {
        TelegramBotApplication application = new TelegramBotApplication();

        properties.getBots().forEach((botName, botProperties) -> {
            if (!botProperties.isEnabled()) {
                return;
            }

            String token = botProperties.getToken();

            if (token == null || token.isBlank()) {
                throw new IllegalArgumentException("Telegram bot token is required for bot: " + botName);
            }

            TelegramBotConfig config = TelegramBotConfig.builder()
                    .handlerRegistrationMode(HandlerRegistrationMode.EXTERNAL)
                    .build();

            application.register(botName, token, config);
        });

        return application;
    }
}
