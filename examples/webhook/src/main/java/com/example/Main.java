package com.example;

import io.github.solmosov.telegrambot.bot.TelegramBot;

public class Main {

    public static void main(String[] args) {
        TelegramBot myBot = new TelegramBot("myBot", System.getenv("MY_BOT_TOKEN"));

        myBot.start();
    }
}