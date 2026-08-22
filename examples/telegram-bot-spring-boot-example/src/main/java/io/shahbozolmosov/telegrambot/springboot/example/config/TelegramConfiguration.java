package io.shahbozolmosov.telegrambot.springboot.example.config;

import io.github.shahbozolmosov.telegrambot.spring.boot.TelegramBotRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramConfiguration {

    @Bean
    public TelegramBotRegistration supportBot() {
        return new TelegramBotRegistration(
                "support",
                System.getenv("TELEGRAM_BOT_SUPPORT_TOKEN")
        );
    }

    @Bean TelegramBotRegistration adminBot(){
        return new TelegramBotRegistration(
                "admin",
                System.getenv("TELEGRAM_BOT_ADMIN_TOKEN")
        );
    }
}
