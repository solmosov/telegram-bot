package io.github.shahbozolmosov;

import io.github.shahbozolmosov.bot.ExecutionMode;
import io.github.shahbozolmosov.bot.TelegramBot;
import io.github.shahbozolmosov.bot.TelegramBotConfig;
import io.github.shahbozolmosov.bot.UpdatesMode;

public class Main {
    public static void main(String[] args) {

        String token = System.getenv("TELEGRAM_BOT_TOKEN");

        TelegramBotConfig config = TelegramBotConfig.builder()
                .shutdownTimeout(4000)
                .updateMode(UpdatesMode.WEBHOOK)
                .executionMode(ExecutionMode.SINGLE_THREAD)
                .webhookPort(8080)
                .webhookPath("/webhook/telegram")
                .webhookUrl("https://miscellaneous-fingers-levels-responsibilities.trycloudflare.com/webhook/telegram")
                .build();

        TelegramBot bot = new TelegramBot(token, config);

        bot.start();
    }
}