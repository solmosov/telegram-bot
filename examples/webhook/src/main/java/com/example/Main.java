package com.example;

import io.github.solmosov.telegrambot.bot.TelegramBot;
import io.github.solmosov.telegrambot.bot.TelegramBotConfig;
import io.github.solmosov.telegrambot.bot.UpdatesMode;

public class Main {

    public static void main(String[] args) {

        TelegramBotConfig myBotConfig = TelegramBotConfig.builder()
                .updateMode(UpdatesMode.WEBHOOK)
                .webhookPort(8080)
                .webhookPath("/webhook/telegram")
                .webhookPathSecret(System.getenv("MY_BOT_WEBHOOK_PATH_SECRET"))
                .webhookSecret(System.getenv("MY_BOT_WEBHOOK_SECRET"))
                .webhookUrl(System.getenv("MY_BOT_WEBHOOK_URL"))
                .build();

        TelegramBot myBot = new TelegramBot("myBot", System.getenv("MY_BOT_TOKEN"), myBotConfig);

        myBot.start();
    }
}