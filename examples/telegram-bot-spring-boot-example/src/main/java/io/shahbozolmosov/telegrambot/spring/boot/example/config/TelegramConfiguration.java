package io.shahbozolmosov.telegrambot.spring.boot.example.config;

import io.github.shahbozolmosov.telegrambot.bot.HandlerRegistrationMode;
import io.github.shahbozolmosov.telegrambot.bot.TelegramBotConfig;
import io.github.shahbozolmosov.telegrambot.spring.boot.TelegramBotRegistration;
import io.shahbozolmosov.telegrambot.spring.boot.example.security.MyAuthorizationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramConfiguration {

    @Bean
    public TelegramBotRegistration supportBot() {
        return TelegramBotRegistration.builder()
                .botName("support")
                .token(System.getenv("TELEGRAM_BOT_SUPPORT_TOKEN"))
                .build();
    }

    @Bean
    TelegramBotRegistration adminBot() {
        return TelegramBotRegistration.builder()
                .botName("admin")
                .token(System.getenv("TELEGRAM_BOT_ADMIN_TOKEN"))
                .config(config -> {
                    config.authorizationProvider(new MyAuthorizationProvider());
                })
                .build();
    }
}
