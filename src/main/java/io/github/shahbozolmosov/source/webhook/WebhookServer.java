package io.github.shahbozolmosov.source.webhook;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.exception.GlobalExceptionHandler;
import io.github.shahbozolmosov.executor.UpdateExecutor;
import io.github.shahbozolmosov.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class WebhookServer {

    private static final Logger log = LoggerFactory.getLogger(WebhookServer.class);

    private final String host;
    private final int port;
    private final String path;

    private final UpdateExecutor updateExecutor;
    private final Dispatcher dispatcher;
    private final TelegramClient telegramClient;
    private final GlobalExceptionHandler globalExceptionHandler;

    private HttpServer server;

    private final ObjectMapper objectMapper;

    public WebhookServer(
            String host,
            int port,
            String path,
            UpdateExecutor updateExecutor,
            Dispatcher dispatcher,
            JsonMapper jsonMapper,
            TelegramClient telegramClient,

            GlobalExceptionHandler globalExceptionHandler
    ) {
        this.host = host;
        this.port = port;
        this.path = path;
        this.updateExecutor = updateExecutor;
        this.dispatcher = dispatcher;

        this.globalExceptionHandler = globalExceptionHandler;

        this.objectMapper = jsonMapper;

        this.telegramClient = telegramClient;
    }

    public void start() {
        try {
            server = HttpServer.create(
                    new InetSocketAddress(host, port),
                    0
            );

            server.createContext(path, this::handle);

            server.start();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to start webhook server", ex);
        }
    }

    private void handle(HttpExchange httpExchange) throws IOException {
        if (!"POST".equalsIgnoreCase(httpExchange.getRequestMethod())) {
            sendResponse(httpExchange, 405, "");
            return;
        }

        String body;

        try (InputStream inputStream = httpExchange.getRequestBody()) {
            body = new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }

        Update update = parseUpdate(body);

        long chatId = extractChatId(update);

        updateExecutor.submit(chatId, () -> {
            BotContext context = new BotContext(telegramClient, update);

            try {
                log.debug("Processing update: {}", update.updateId());


                dispatcher.dispatch(update, context);
            } catch (Exception ex) {
                globalExceptionHandler.handle(ex, update, context);
            }
        });

        sendResponse(httpExchange, 200, "OK");
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

    private Update parseUpdate(String body) {
        return objectMapper.readValue(body, Update.class);
    }

    private void sendResponse(HttpExchange httpExchange, int status, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        httpExchange.sendResponseHeaders(status, bytes.length);

        try (OutputStream output = httpExchange.getResponseBody()) {
            output.write(bytes);
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    public void shutdown() {
        stop();
    }
}
