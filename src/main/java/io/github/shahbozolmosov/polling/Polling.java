package io.github.shahbozolmosov.polling;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.model.Update;

import java.util.List;

public class Polling {

    private final TelegramClient client;
    private final Dispatcher dispatcher;

    private long offset;

    public Polling(
            TelegramClient client,
            Dispatcher dispatcher
    ){
        this.client = client;
        this.dispatcher = dispatcher;
    }

    public void start(){
        while (!Thread.currentThread().isInterrupted()){
            poll();
        }
    }

    private void poll(){
        TelegramResponse<List<Update>> response = client.getUpdates(offset);

        for(Update update : response.result()){

            BotContext context = new BotContext(
                    client,
                    update
            );

            dispatcher.dispatch(update, context);

            offset  = update.updateId() + 1;


            System.out.println("[Telegram Bot] Processing update: " + update.updateId());
        }
    }


}
