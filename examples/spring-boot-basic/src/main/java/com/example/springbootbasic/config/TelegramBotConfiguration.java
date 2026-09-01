package com.example.springbootbasic.config;

import io.github.solmosov.telegrambot.spring.boot.TelegramBotRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfiguration {

    @Bean
    public TelegramBotRegistration myBotRegistration(){
        return TelegramBotRegistration.builder()
                .botName("myBot")
                .token(System.getenv("MY_BOT_TOKEN"))
                .build();
    }
}
