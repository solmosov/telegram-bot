package io.github.shahbozolmosov.source.webhook;

import io.github.shahbozolmosov.bot.ExecutionMode;
import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.executor.SingleThreadUpdateExecutor;
import io.github.shahbozolmosov.executor.UpdateExecutor;
import io.github.shahbozolmosov.executor.VirtualThreadUpdateExecutor;
import io.github.shahbozolmosov.source.UpdateSource;
import tools.jackson.databind.json.JsonMapper;

public class WebhookUpdateSource implements UpdateSource {

    private final TelegramClient client;
    private final UpdateExecutor updateExecutor;
    private final WebhookServer server;

    private final String url;

    public WebhookUpdateSource(
            TelegramClient client,
            Dispatcher dispatcher,
            ExecutionMode executionMode,
            JsonMapper jsonMapper,

            String host,
            int port,
            String path,
            String url
    ) {
        this.client = client;
        this.url = url;

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
                client
        );
    }

    @Override
    public void start() {
        var response = client.setWebhook(url);
        System.out.println("setWebhook response: " + response);

        server.start();
    }

    @Override
    public void stop() {
        server.stop();
    }

    @Override
    public void shutdown() {
        server.shutdown();
        updateExecutor.shutdown();
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.WEBHOOK;
    }
}
