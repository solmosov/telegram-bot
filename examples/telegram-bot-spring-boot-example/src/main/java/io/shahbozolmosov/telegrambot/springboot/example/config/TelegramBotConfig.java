package io.shahbozolmosov.telegrambot.springboot.example.config;

import io.github.shahbozolmosov.telegrambot.authorization.AuthorizationProvider;
import io.shahbozolmosov.telegrambot.springboot.example.security.MyAuthorizationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {

    @Bean
    public AuthorizationProvider authorizationProvider() {
        return new MyAuthorizationProvider();
    }
}
