package io.github.shahbozolmosov;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.model.User;

public class Main {
    public static void main(String[] args) throws Exception {
        TelegramClient client = new TelegramClient("8821261401:AAFy2Tdl9ajQdsPswInQ29gpVjULitoVTe4");

        TelegramResponse<User> response = client.getMe();

        System.out.println(response.ok());
        System.out.println(response.result().username());
    }
}