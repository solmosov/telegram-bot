package com.example.springbootwebhook.config;

import io.github.solmosov.telegrambot.bot.UpdatesMode;
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
                .config(config -> config
                        .updateMode(UpdatesMode.WEBHOOK)
                        .webhookPort(8081)
                        .webhookPath("/webhook/telegram") // path
                        .webhookPathSecret(System.getenv("MY_BOT_WEBHOOK_PATH_SECRET")) // your secret for path protection
                        .webhookSecret(System.getenv("MY_BOT_WEBHOOK_SECRET")) // your secret for header
                        .webhookUrl(System.getenv("MY_BOT_WEBHOOK_URL")) // https://your-domain.com
                )
                .build();
    }
}
