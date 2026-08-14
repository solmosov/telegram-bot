package io.github.shahbozolmosov.polling;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.exception.TelegramClientException;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.model.Update;

import java.util.List;

public class Polling {

    private final TelegramClient client;
    private final Dispatcher dispatcher;
    private final ChatSequentialExecutor chatExecutor = new ChatSequentialExecutor();

    private volatile boolean running = true;
    private long offset;

    public Polling(
            TelegramClient client,
            Dispatcher dispatcher
    ) {
        this.client = client;
        this.dispatcher = dispatcher;
    }

    public void start() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                poll();
            } catch (TelegramClientException ex) {
                System.err.println("[Telegram Bot] Polling error: " + ex.getMessage());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void poll() {
        TelegramResponse<List<Update>> response = client.getUpdates(offset);

        for (Update update : response.result()) {
            offset = update.updateId() + 1;

            long chatId = extractChatId(update);

            chatExecutor.submit(chatId, () -> {
                try {
                    BotContext context = new BotContext(client, update);
                    dispatcher.dispatch(update, context);
                    System.out.println("[Telegram Bot] Processing update: " + update.updateId());
                } catch (Exception ex) {
                    System.err.println("[Telegram Bot] Handler error for update "
                            + update.updateId() + ": " + ex.getMessage());
                }
            });
        }
    }

    private long extractChatId(Update update) {
        if (update.message() != null) {
            return update.message().chat().id();
        }

        if (update.callbackQuery() != null) {
            return update.callbackQuery().message().chat().id();
        }

        return update.updateId();
    }

    public void stop() {
        running = false;
        shutdown();
    }

    private void shutdown() {
        chatExecutor.shutdown();
    }

}
