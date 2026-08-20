package io.github.shahbozolmosov;

import io.github.shahbozolmosov.bot.ExecutionMode;
import io.github.shahbozolmosov.bot.TelegramBot;
import io.github.shahbozolmosov.bot.TelegramBotConfig;
import io.github.shahbozolmosov.bot.UpdatesMode;
import io.github.shahbozolmosov.example.authorization.MyAuthorizationProvider;
import io.github.shahbozolmosov.example.exception.MyGlobalExceptionHandler;

public class Main {
    public static void main(String[] args) {

        String token = System.getenv("TELEGRAM_BOT_TOKEN");
        String webhookUrl = System.getenv("WEBHOOK_URL");

        TelegramBotConfig config = TelegramBotConfig.builder()
                .shutdownTimeout(4000)
                .updateMode(UpdatesMode.POLLING)
                .executionMode(ExecutionMode.SINGLE_THREAD)
//                .webhookPort(8080)
//                .webhookPath("/webhook/telegram")
//                .webhookUrl(webhookUrl) // https://example.com/webhook/telegram
                .authorizationProvider(new MyAuthorizationProvider())
                .globalExceptionHandler(new MyGlobalExceptionHandler())
                .build();

        TelegramBot bot = new TelegramBot(token, config);

        bot.start();
    }
}