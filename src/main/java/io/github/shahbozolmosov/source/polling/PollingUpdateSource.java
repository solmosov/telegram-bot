package io.github.shahbozolmosov.source.polling;

import io.github.shahbozolmosov.bot.ExecutionMode;
import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.exception.client.TelegramClientException;
import io.github.shahbozolmosov.exception.GlobalExceptionHandler;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.executor.SingleThreadUpdateExecutor;
import io.github.shahbozolmosov.executor.UpdateExecutor;
import io.github.shahbozolmosov.executor.VirtualThreadUpdateExecutor;
import io.github.shahbozolmosov.source.UpdateSource;

import java.util.List;

public class PollingUpdateSource implements UpdateSource {

    private final TelegramClient client;
    private final Dispatcher dispatcher;
    private final UpdateExecutor updateExecutor;
    private final PollingUpdateHandler updateHandler;
    private final ExecutionMode executionMode;

    private volatile boolean running = true;
    private long offset;
    private Thread pollingThread;

    public PollingUpdateSource(
            TelegramClient client,
            Dispatcher dispatcher,
            ExecutionMode executionMode,
            GlobalExceptionHandler globalExceptionHandler
    ) {
        this.client = client;
        this.dispatcher = dispatcher;
        this.executionMode = executionMode;

        this.updateExecutor = switch (this.executionMode) {
            case SINGLE_THREAD -> new SingleThreadUpdateExecutor();
            case MULTI_VIRTUAL_THREAD -> new VirtualThreadUpdateExecutor();
        };
        this.updateHandler = new PollingUpdateHandler(client, dispatcher, updateExecutor, globalExceptionHandler);
    }

    @Override
    public void start() {
        checkWebhookExists();

        pollingThread = new Thread(() -> {
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
        });

        pollingThread.setName("telegram-polling-thread");
        pollingThread.start();
        System.out.println("[Telegram Bot] Telegram polling started");
        System.out.println("[Telegram Bot] Execution mode: " + this.executionMode.name());
    }

    private void checkWebhookExists() {
        var response = client.getWebhookInfo();

        if (!response.result().url().isEmpty()) {
            var deleteResponse = client.deleteWebhook();
            boolean webhookRemoved = deleteResponse.ok() && deleteResponse.result();
            if (webhookRemoved) {
                System.out.println("[Telegram Bot] The Telegram bot webhook successfully removed for POLLING MODE");
            }else {
                throw new IllegalStateException(
                        "[Telegram Bot] Failed to remove the Telegram bot webhook for POLLING MODE \n. Please remove the webhook URL manually."
                );
            }
        }
    }

    private void poll() {
        TelegramResponse<List<Update>> response = client.getUpdates(offset);

        for (Update update : response.result()) {
            offset = update.updateId() + 1;
            updateHandler.handle(update);
        }
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public void shutdown() {
        System.out.println("[Telegram Bot] Polling Shutting down...");
        if (pollingThread != null && pollingThread.isAlive()) {
            pollingThread.interrupt();
        }
        updateExecutor.shutdown();
        System.out.println("[Telegram Bot] Polling Shutdown completed");
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.POLLING;
    }

    public Thread getPollingThread() {
        return pollingThread;
    }
}
