package io.github.shahbozolmosov;

import io.github.shahbozolmosov.bot.TelegramBot;
import io.github.shahbozolmosov.example.MyBot;

public class Main {
    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot("8821261401:AAFy2Tdl9ajQdsPswInQ29gpVjULitoVTe4");


        bot.registerCommands(new MyBot());

        bot.start();
    }
}