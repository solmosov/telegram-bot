package io.github.shahbozolmosov;

import io.github.shahbozolmosov.bot.TelegramBot;
import io.github.shahbozolmosov.scanner.ClassInstanceFactory;
import io.github.shahbozolmosov.scanner.ClassScanner;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot("8821261401:AAFy2Tdl9ajQdsPswInQ29gpVjULitoVTe4");

        bot.registerCommands();
        bot.start();
    }
}