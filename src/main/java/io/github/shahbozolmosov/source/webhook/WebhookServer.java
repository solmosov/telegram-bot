package io.github.shahbozolmosov.source.webhook;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.exception.GlobalExceptionHandler;
import io.github.shahbozolmosov.exception.webhook.RequestBodyTooLargeException;
import io.github.shahbozolmosov.executor.UpdateExecutor;
import io.github.shahbozolmosov.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.concurrent.RejectedExecutionException;

public class WebhookServer {

    private static final Logger log = LoggerFactory.getLogger(WebhookServer.class);

    private static final int MAX_REQUEST_BODY_SIZE = 256 * 1024; // 256 KB

    private final String botName;

    private final String host;
    private final int port;
    private final String path;
    private final String secret;

    private final UpdateExecutor updateExecutor;
    private final Dispatcher dispatcher;
    private final TelegramClient telegramClient;
    private final GlobalExceptionHandler globalExceptionHandler;

    private HttpServer server;

    private final ObjectMapper objectMapper;

    public WebhookServer(
            String botName,

            String host,
            int port,
            String path,
            String secret,

            UpdateExecutor updateExecutor,
            Dispatcher dispatcher,
            JsonMapper jsonMapper,
            TelegramClient telegramClient,

            GlobalExceptionHandler globalExceptionHandler
    ) {
        this.botName = botName;

        this.host = host;
        this.port = port;
        this.path = path;
        this.secret = secret;

        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException("Secret is required");
        }

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
        // Method Type
        if (!"POST".equalsIgnoreCase(httpExchange.getRequestMethod())) {
            sendResponse(httpExchange, 405, "");
            return;
        }

        // Secret
        String receivedSecret = httpExchange.getRequestHeaders()
                .getFirst("X-Telegram-Bot-Api-Secret-Token");

        if (
                receivedSecret == null
                        || receivedSecret.isBlank()
                        || !MessageDigest.isEqual(
                        receivedSecret.getBytes(StandardCharsets.UTF_8),
                        secret.getBytes(StandardCharsets.UTF_8)
                )
        ) {
            sendResponse(httpExchange, 403, "");
            return;
        }

        // Content Type
        String contentType = httpExchange.getRequestHeaders()
                .getFirst("Content-Type");

        if (contentType == null || !contentType.toLowerCase().startsWith("application/json")) {
            sendResponse(httpExchange, 415, "");
            return;
        }

        // Content Length
        String contentLength = httpExchange.getRequestHeaders()
                .getFirst("Content-Length");

        if (contentLength != null) {
            try {
                long length = Long.parseLong(contentLength);

                if (length > MAX_REQUEST_BODY_SIZE) {
                    sendResponse(httpExchange, 413, "");
                    return;
                }
            } catch (NumberFormatException ex) {
                sendResponse(httpExchange, 400, "");
                return;
            }
        }

        // Body
        String body;

        try (InputStream inputStream = httpExchange.getRequestBody()) {
            try {
                body = readRequestBody(inputStream);
            } catch (RequestBodyTooLargeException ex) {
                sendResponse(httpExchange, 413, "");
                return;
            }
        }

        // Update
        Update update;

        try {
            update = parseUpdate(body);
        } catch (Exception ex) {
            log.warn("Invalid webhook request body", ex);
            sendResponse(httpExchange, 400, "");
            return;
        }

        long chatId = extractChatId(update);

        try {
            updateExecutor.submit(chatId, () -> {
                MDC.put("bot", botName);
                try {
                    BotContext context = new BotContext(telegramClient, update);

                    try {
                        log.debug("Processing update: {}", update.updateId());


                        dispatcher.dispatch(update, context);
                    } catch (Exception ex) {
                        globalExceptionHandler.handle(ex, update, context);
                    }
                } finally {
                    MDC.remove("bot");
                }
            });
        } catch (RejectedExecutionException ex) {
            log.warn("Update queue is full for chat {}", chatId);
            sendResponse(httpExchange, 503, "");
            return;
        }

        sendResponse(httpExchange, 200, "OK");
    }

    private String readRequestBody(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[16384];
        int totalBytes = 0;

        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            totalBytes += bytesRead;

            if (totalBytes > MAX_REQUEST_BODY_SIZE) {
                throw new RequestBodyTooLargeException();
            }

            outputStream.write(buffer, 0, bytesRead);
        }

        return outputStream.toString(StandardCharsets.UTF_8);
    }

    private long extractChatId(Update update) {
        if (update.message() != null) {
            return update.message().chat().id();
        }

        if (update.callbackQuery() != null) {
            return update.callbackQuery().from().id();
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
