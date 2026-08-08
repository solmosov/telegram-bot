package io.github.shahbozolmosov;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.model.User;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        TelegramClient client = new TelegramClient("8821261401:AAFy2Tdl9ajQdsPswInQ29gpVjULitoVTe4");

        TelegramResponse<User> response = client.getMe();

        System.out.println(response.ok());
        System.out.println(response.result().username());

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        Runnable task = () -> {
            TelegramResponse<List<Update>> res =  client.getUpdates();
            System.out.println("Task executed at: " + LocalTime.now() + "res: " + res.result().size());
        };

        scheduler.scheduleAtFixedRate(task, 0, 3, TimeUnit.SECONDS);

    }
}