package io.github.shahbozolmosov.telegrambot.source.polling;

import io.github.shahbozolmosov.telegrambot.bot.ExecutionMode;
import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.dispatcher.Dispatcher;
import io.github.shahbozolmosov.telegrambot.exception.client.TelegramClientException;
import io.github.shahbozolmosov.telegrambot.exception.GlobalExceptionHandler;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.model.Update;
import io.github.shahbozolmosov.telegrambot.executor.SingleThreadUpdateExecutor;
import io.github.shahbozolmosov.telegrambot.executor.UpdateExecutor;
import io.github.shahbozolmosov.telegrambot.executor.MultiVirtualThreadUpdateExecutor;
import io.github.shahbozolmosov.telegrambot.source.UpdateSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PollingUpdateSource implements UpdateSource {

    private static final Logger log = LoggerFactory.getLogger(PollingUpdateSource.class);

    private final TelegramClient client;
    private final UpdateExecutor updateExecutor;
    private final PollingUpdateHandler updateHandler;
    private final ExecutionMode executionMode;

    private volatile boolean running = true;
    private long offset;
    private Thread pollingThread;

    public PollingUpdateSource(
            String botName,
            TelegramClient client,
            Dispatcher dispatcher,
            ExecutionMode executionMode,
            GlobalExceptionHandler globalExceptionHandler,
            long processingTimeout
    ) {
        this.client = client;
        this.executionMode = executionMode;

        this.updateExecutor = switch (this.executionMode) {
            case SINGLE_THREAD -> new SingleThreadUpdateExecutor(processingTimeout);
            case MULTI_VIRTUAL_THREAD -> new MultiVirtualThreadUpdateExecutor(processingTimeout);
        };
        this.updateHandler = new PollingUpdateHandler(
                botName,
                client,
                dispatcher,
                updateExecutor,
                globalExceptionHandler
        );
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
                        log.error("Polling error: {}", ex.getMessage());

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
        log.info("Telegram polling started");
        log.info("Execution mode: {}", this.executionMode.name());
    }

    private void checkWebhookExists() {
        var response = client.getWebhookInfo();

        if (!response.result().url().isEmpty()) {
            var deleteResponse = client.deleteWebhook();
            boolean webhookRemoved = deleteResponse.ok() && deleteResponse.result();
            if (webhookRemoved) {
                log.info("The Telegram bot webhook successfully removed for POLLING MODE");
            } else {
                throw new IllegalStateException(
                        "Failed to remove the Telegram bot webhook for POLLING MODE \n. Please remove the webhook URL manually."
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
        log.info("Polling Shutting down...");
        if (pollingThread != null && pollingThread.isAlive()) {
            pollingThread.interrupt();
        }
        updateExecutor.shutdown();
        log.info("Polling Shutdown completed");
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.POLLING;
    }

    public Thread getPollingThread() {
        return pollingThread;
    }
}
