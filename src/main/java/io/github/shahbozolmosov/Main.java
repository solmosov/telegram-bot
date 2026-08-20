package io.github.shahbozolmosov;

import io.github.shahbozolmosov.bot.ExecutionMode;
import io.github.shahbozolmosov.bot.TelegramBot;
import io.github.shahbozolmosov.bot.TelegramBotConfig;
import io.github.shahbozolmosov.bot.UpdatesMode;
import io.github.shahbozolmosov.example.authorization.MyAuthorizationProvider;
import io.github.shahbozolmosov.example.exception.MyGlobalExceptionHandler;

public class Main {
    public static void main(String[] args) {

        // Support Bot
        String botName = "support";
        String supportToken = System.getenv("TELEGRAM_BOT_SUPPORT_TOKEN");
        String supportWebhookUrl = System.getenv("TELEGRAM_BOT_SUPPORT_WEBHOOK_URL");

        TelegramBotConfig supportConfig = TelegramBotConfig.builder()
                .shutdownTimeout(4000)
                .updateMode(UpdatesMode.WEBHOOK)
                .executionMode(ExecutionMode.SINGLE_THREAD)
                .webhookPort(8080)
                .webhookPath("/webhook/telegram")
                .webhookUrl(supportWebhookUrl) // https://example.com/webhook/telegram
                .authorizationProvider(new MyAuthorizationProvider())
                .globalExceptionHandler(new MyGlobalExceptionHandler())
                .build();

        TelegramBot supportBot = new TelegramBot(botName, supportToken, supportConfig);

        supportBot.start();

        // Admin Bot
        String token = System.getenv("TELEGRAM_BOT_ADMIN_TOKEN");

        TelegramBotConfig adminConfig = TelegramBotConfig.builder()
                .authorizationProvider(new MyAuthorizationProvider())
                .build();

        TelegramBot adminBot = new TelegramBot("admin", token, adminConfig);

        adminBot.start();
    }
}