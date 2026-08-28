package io.github.solmosov.telegrambot.spring.boot;

import io.github.solmosov.telegrambot.bot.HandlerRegistrationMode;
import io.github.solmosov.telegrambot.bot.TelegramBotConfig;

import java.util.Objects;
import java.util.function.Consumer;

public class TelegramBotRegistration {

    private String botName;
    private String token;
    private TelegramBotConfig config;

    private TelegramBotRegistration(
            String botName,
            String token,
            TelegramBotConfig config
    ) {
        this.botName = botName;
        this.token = token;
        this.config = config;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String botName() {
        return botName;
    }

    public String token() {
        return token;
    }

    public TelegramBotConfig config() {
        return config;
    }

    public static class Builder {
        private String botName;
        private String token;
        private TelegramBotConfig.Builder config = TelegramBotConfig.builder()
                .handlerRegistrationMode(HandlerRegistrationMode.EXTERNAL);

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

        public Builder config(Consumer<TelegramBotConfig.Builder> customizer) {
            Objects.requireNonNull(customizer, "customizer must not be null");

            customizer.accept(config);

            return this;
        }

        public TelegramBotRegistration build() {
            if (botName == null || botName.isBlank()) {
                throw new IllegalArgumentException(
                        "Bot name must not be blank"
                );
            }

            if (token == null || token.isBlank()) {
                throw new IllegalArgumentException(
                        "Bot token must not be blank for bot: " + botName
                );
            }

            return new TelegramBotRegistration(botName, token, config.build());
        }
    }
}

