package io.github.shahbozolmosov.polling;

import io.github.shahbozolmosov.bot.ExecutionMode;
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
    private final UpdateExecutor updateExecutor;

    private volatile boolean running = true;
    private long offset;

    public Polling(
            TelegramClient client,
            Dispatcher dispatcher,
            ExecutionMode executionMode
    ) {
        this.client = client;
        this.dispatcher = dispatcher;
        this.updateExecutor = switch (executionMode) {
            case SINGLE_THREAD -> new SingleThreadUpdateExecutor();
            case VIRTUAL_THREAD -> new VirtualThreadUpdateExecutor();
        };
    }

    public void start() {
        try {
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
        } finally {
            shutdown();
        }
    }

    private void poll() {
        TelegramResponse<List<Update>> response = client.getUpdates(offset);

        for (Update update : response.result()) {
            offset = update.updateId() + 1;


            long chatId = extractChatId(update);

            updateExecutor.submit(chatId, () -> {
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
    }

    public void shutdown() {
        System.out.println("[Telegram Bot] Polling Shutting down...");
        updateExecutor.shutdown();
        System.out.println("[Telegram Bot] Polling Shutdown completed");
    }

}
