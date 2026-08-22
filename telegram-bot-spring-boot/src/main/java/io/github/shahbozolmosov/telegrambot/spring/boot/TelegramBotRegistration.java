package io.github.shahbozolmosov.telegrambot.spring.boot;

import io.github.shahbozolmosov.telegrambot.bot.TelegramBotConfig;

public record TelegramBotRegistration(
        String botName,
        String token,
        TelegramBotConfig config
) {
    public TelegramBotRegistration(String botName, String token) {
        this(botName, token, null);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String botName;
        private String token;
        private TelegramBotConfig config;

        private Builder() {
        }

        public void setBotName(String name) {
            this.botName = name;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public void setConfig(TelegramBotConfig config) {
            this.config = config;
        }

        public TelegramBotRegistration build() {
            return new TelegramBotRegistration(botName, token, config);
        }
    }
}

