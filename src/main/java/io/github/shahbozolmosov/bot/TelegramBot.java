package io.github.shahbozolmosov.bot;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.model.Update;

import java.util.List;

public final class TelegramBot {

    private final TelegramClient telegramClient;

    public TelegramBot(String botToken) {
        this.telegramClient = new TelegramClient(botToken);
    }

    public void start() {
        long offset = 0;

        while (true){
            TelegramResponse<List<Update>> res = telegramClient.getUpdates(offset);

            for (Update update : res.result()) {
                offset = update.updateId() + 1;

                Message message = update.message();

                if (message != null && "/start".equals(message.text())) {
                    telegramClient.sendMessage(
                            message.chat().id(),
                            "Welcome, Hello World"
                    );
                }



                System.out.println("Processing update: " + update.updateId());
            }
        }
    }

}
