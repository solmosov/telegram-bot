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

        public Builder botName(String name) {
            this.botName = name;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder config(TelegramBotConfig config) {
            this.config = config;
            return this;
        }

        public TelegramBotRegistration build() {
            return new TelegramBotRegistration(botName, token, config);
        }
    }
}

