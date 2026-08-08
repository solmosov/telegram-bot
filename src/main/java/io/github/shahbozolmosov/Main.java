package io.github.shahbozolmosov;

import io.github.shahbozolmosov.bot.TelegramBot;
import io.github.shahbozolmosov.example.MyBot;
import io.github.shahbozolmosov.scanner.ClassScanner;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot("8821261401:AAFy2Tdl9ajQdsPswInQ29gpVjULitoVTe4");

//
//        bot.registerCommands(new MyBot());
//        bot.start();


        ClassScanner scanner = new ClassScanner();

        List<Class<?>> classes = scanner.scan("io.github.shahbozolmosov.example");

        System.out.println("render");
        for (Class<?> clazz : classes) {
            System.out.println(clazz.getName());
        }

    }
}