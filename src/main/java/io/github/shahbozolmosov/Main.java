package io.github.shahbozolmosov;

import io.github.shahbozolmosov.bot.*;
import io.github.shahbozolmosov.example.authorization.MyAuthorizationProvider;
import io.github.shahbozolmosov.example.exception.MyGlobalExceptionHandler;

public class Main {
    public static void main(String[] args) {

        // Support Bot Config
        String supportToken = System.getenv("TELEGRAM_BOT_SUPPORT_TOKEN");

        TelegramBotConfig supportConfig = TelegramBotConfig.builder()
                .shutdownTimeout(4000)
                .updateMode(UpdatesMode.WEBHOOK)
                .executionMode(ExecutionMode.SINGLE_THREAD)
                .authorizationProvider(new MyAuthorizationProvider())
                .globalExceptionHandler(new MyGlobalExceptionHandler())
                .build();


        // Admin Bot Config
        String adminToken = System.getenv("TELEGRAM_BOT_ADMIN_TOKEN");

        TelegramBotConfig adminConfig = TelegramBotConfig.builder()
                .authorizationProvider(new MyAuthorizationProvider())
                .build();

        // Application
        TelegramBotApplication application = new TelegramBotApplication();

        application
                .register("support", supportToken, supportConfig)
                .register("admin", adminToken, adminConfig);


        application.start();
    }
}