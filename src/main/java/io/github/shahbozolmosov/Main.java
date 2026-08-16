package io.github.shahbozolmosov;

import io.github.shahbozolmosov.bot.ExecutionMode;
import io.github.shahbozolmosov.bot.TelegramBot;
import io.github.shahbozolmosov.bot.TelegramBotConfig;

public class Main {
    public static void main(String[] args) {

        String token = System.getenv("TELEGRAM_BOT_TOKEN");

        TelegramBotConfig config = TelegramBotConfig.builder()
                .shutdownTimeout(4000)
                .executionMode(ExecutionMode.MULTI_VIRTUAL_THREAD)
                .build();

        TelegramBot bot = new TelegramBot(token, config);

        bot.start();
    }
}