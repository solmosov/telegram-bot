package io.github.shahbozolmosov.source.webhook;

import io.github.shahbozolmosov.bot.ExecutionMode;
import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.exception.GlobalExceptionHandler;
import io.github.shahbozolmosov.exception.api.TelegramApiException;
import io.github.shahbozolmosov.exception.client.TelegramClientException;
import io.github.shahbozolmosov.exception.api.WebhookSetupException;
import io.github.shahbozolmosov.executor.SingleThreadUpdateExecutor;
import io.github.shahbozolmosov.executor.UpdateExecutor;
import io.github.shahbozolmosov.executor.VirtualThreadUpdateExecutor;
import io.github.shahbozolmosov.source.UpdateSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.json.JsonMapper;

public class WebhookUpdateSource implements UpdateSource {

    private static final Logger log = LoggerFactory.getLogger(WebhookUpdateSource.class);

    private final TelegramClient client;
    private final UpdateExecutor updateExecutor;
    private final WebhookServer server;
    private final ExecutionMode executionMode;

    private final String url;

    public WebhookUpdateSource(
            TelegramClient client,
            Dispatcher dispatcher,
            ExecutionMode executionMode,
            JsonMapper jsonMapper,

            String host,
            int port,
            String path,
            String url,

            GlobalExceptionHandler globalExceptionHandler
    ) {
        this.client = client;
        this.url = url;
        this.executionMode = executionMode;

        this.updateExecutor = switch (executionMode) {
            case SINGLE_THREAD -> new SingleThreadUpdateExecutor();
            case MULTI_VIRTUAL_THREAD -> new VirtualThreadUpdateExecutor();
        };

        this.server = new WebhookServer(
                host,
                port,
                path,
                updateExecutor,
                dispatcher,
                jsonMapper,
                client,

                globalExceptionHandler
        );
    }

    @Override
    public void start() {
        setWebhook(url);

        server.start();
        log.info("Telegram webhook started");
        log.info("Telegram Execution mode: {}", this.executionMode.name());
    }

    private void setWebhook(String url) {
        try {
            var response = client.setWebhook(url);

            log.debug("setWebhook response: {}", response);
        } catch (TelegramApiException ex) {
            throw new WebhookSetupException(ex);
        } catch (Exception ex) {
            log.error("Exception {}", ex.getMessage());
        }
    }

    @Override
    public void stop() {
        server.stop();
    }

    @Override
    public void shutdown() {
        log.info("Webhook Shutting down...");
        server.shutdown();
        updateExecutor.shutdown();
        log.info("Webhook Shutdown completed");
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.WEBHOOK;
    }
}
