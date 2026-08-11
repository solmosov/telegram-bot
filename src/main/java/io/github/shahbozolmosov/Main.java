package io.github.shahbozolmosov;

import io.github.shahbozolmosov.bot.TelegramBot;

public class Main {
    public static void main(String[] args) {

        String token = System.getenv("TELEGRAM_BOT_TOKEN");

        TelegramBot bot = new TelegramBot(token);

        bot.start();
    }
}