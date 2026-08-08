package io.github.shahbozolmosov;

import io.github.shahbozolmosov.client.TelegramClient;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        TelegramClient client = new TelegramClient("8821261401:AAFy2Tdl9ajQdsPswInQ29gpVjULitoVTe4");

        String response = client.getMe();

        System.out.println(response);
    }
}