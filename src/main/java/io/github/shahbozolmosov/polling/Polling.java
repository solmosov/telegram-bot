package io.github.shahbozolmosov.polling;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.exception.TelegramClientException;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.model.Update;
import tools.jackson.databind.ser.jdk.IterableSerializer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Polling {

    private final TelegramClient client;
    private final Dispatcher dispatcher;
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

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

            executorService.submit(() -> {
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

    public void stop() {
        running = false;
        shutdown();
    }

    private void shutdown() {
        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                System.err.println("[Telegram Bot] Task did not finish within 30s, forcing shutdown");
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
