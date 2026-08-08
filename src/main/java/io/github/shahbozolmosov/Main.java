package io.github.shahbozolmosov;

import io.github.shahbozolmosov.bot.TelegramBot;
import io.github.shahbozolmosov.scanner.ClassInstanceFactory;
import io.github.shahbozolmosov.scanner.ClassScanner;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot("8821261401:AAFy2Tdl9ajQdsPswInQ29gpVjULitoVTe4");

        ClassScanner scanner = new ClassScanner();
        ClassInstanceFactory factory = new ClassInstanceFactory();

        List<Class<?>> classes = scanner.scan("io.github.shahbozolmosov.example");

        for (Class<?> clazz : classes) {

            Object instance = factory.create(clazz);
            bot.registerCommands(instance);
        }

        bot.start();
    }
}