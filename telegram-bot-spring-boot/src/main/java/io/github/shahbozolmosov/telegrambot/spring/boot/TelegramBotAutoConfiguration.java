package io.github.shahbozolmosov.telegrambot.spring.boot;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(TelegramBotProperties.class)
public class TelegramBotAutoConfiguration {
}
