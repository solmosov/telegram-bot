package io.github.solmosov.telegrambot.source.webhook;

import io.github.solmosov.telegrambot.bot.ExecutionMode;
import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.dispatcher.Dispatcher;
import io.github.solmosov.telegrambot.exception.GlobalExceptionHandler;
import io.github.solmosov.telegrambot.exception.api.TelegramApiException;
import io.github.solmosov.telegrambot.exception.api.WebhookSetupException;
import io.github.solmosov.telegrambot.executor.MultiVirtualThreadUpdateExecutor;
import io.github.solmosov.telegrambot.executor.SingleThreadUpdateExecutor;
import io.github.solmosov.telegrambot.executor.UpdateExecutor;
import io.github.solmosov.telegrambot.source.UpdateSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.json.JsonMapper;

import java.util.Optional;

public class WebhookUpdateSource implements UpdateSource {

    private static final Logger log = LoggerFactory.getLogger(WebhookUpdateSource.class);

    private final TelegramClient client;
    private final UpdateExecutor updateExecutor;
    private final WebhookReceiver receiver;
    private final WebhookServer server;
    private final ExecutionMode executionMode;
    private final WebhookServerMode serverMode;
    private final WebhookUrl webhookUrl;
    private final String secret;

    public WebhookUpdateSource(
            String botName,
            TelegramClient client,
            Dispatcher dispatcher,
            ExecutionMode executionMode,
            JsonMapper jsonMapper,
            WebhookServerMode serverMode,

            String host,
            int port,
            String path,
            String url,
            String pathSecret,
            String secret,

            GlobalExceptionHandler globalExceptionHandler,
            long processingTimeout
    ) {
        this.client = client;
        this.secret = secret;
        this.executionMode = executionMode;
        this.serverMode = serverMode;

        this.webhookUrl = new WebhookUrl(url, path, botName, pathSecret);

        this.updateExecutor = switch (executionMode) {
            case SINGLE_THREAD -> new SingleThreadUpdateExecutor(processingTimeout);
            case MULTI_VIRTUAL_THREAD -> new MultiVirtualThreadUpdateExecutor(processingTimeout);
        };

        this.receiver = new WebhookReceiver(
                botName,
                webhookUrl.serverPath(),
                secret,
                updateExecutor,
                dispatcher,
                jsonMapper,
                client,
                globalExceptionHandler
        );

        this.server = serverMode == WebhookServerMode.EMBEDDED
                ? new WebhookServer(host, port, receiver)
                : null;
    }

    public Optional<WebhookReceiver> externalReceiver() {
        return serverMode == WebhookServerMode.EXTERNAL
                ? Optional.of(receiver)
                : Optional.empty();
    }

    @Override
    public void start() {
        receiver.start();

        if (server != null) {
            server.start();
        }

        setWebhook(webhookUrl.fullUrl(), secret);

        log.info("Telegram webhook started");
        log.info("Telegram webhook server mode: {}", serverMode.name());
        log.info("Telegram Execution mode: {}", executionMode.name());
    }

    private void setWebhook(String url, String secret) {
        log.info("setWebhook setting started...");
        try {
            var response = client.setWebhook(url, secret);

            log.debug("setWebhook response: {}", response);
        } catch (TelegramApiException ex) {
            throw new WebhookSetupException(ex);
        } catch (Exception ex) {
            log.error("setWebhook failed {}", ex.getMessage());
        }finally {
            log.info("setWebhook setting completed");
        }
    }

    @Override
    public void stop() {
        if (server != null) {
            server.stop();
        }
    }

    @Override
    public void shutdown() {
        log.info("Webhook Shutting down...");

        if (server != null) {
            server.shutdown();
        }

        receiver.shutdown();
        updateExecutor.shutdown();

        log.info("Webhook Shutdown completed");
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.WEBHOOK;
    }
}