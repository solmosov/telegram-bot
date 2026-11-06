package io.github.solmosov.telegrambot.source.webhook;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.github.solmosov.telegrambot.exception.webhook.RequestBodyTooLargeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class WebhookServer {

    private static final Logger log = LoggerFactory.getLogger(WebhookServer.class);

    private final String host;
    private final int port;
    private final WebhookReceiver receiver;

    private HttpServer server;

    public WebhookServer(String host, int port, WebhookReceiver receiver) {
        this.host = host;
        this.port = port;
        this.receiver = receiver;
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(host, port), 0);
            server.createContext(receiver.path(), this::handle);
            server.start();

            log.info("Webhook server started on {}:{}{}", host, port, receiver.path());
        } catch (IOException ex) {
            throw new RuntimeException("Failed to start webhook server", ex);
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

    private void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            respond(exchange, 405);
            return;
        }

        String contentLength = exchange.getRequestHeaders().getFirst("Content-Length");

        if (contentLength != null) {
            try {
                if (Long.parseLong(contentLength) > WebhookReceiver.MAX_REQUEST_BODY_SIZE) {
                    respond(exchange, 413);
                    return;
                }
            } catch (NumberFormatException ex) {
                respond(exchange, 400);
                return;
            }
        }

        byte[] body;

        try (InputStream inputStream = exchange.getRequestBody()) {
            body = readBody(inputStream);
        } catch (RequestBodyTooLargeException ex) {
            respond(exchange, 413);
            return;
        }

        int status = receiver.receive(
                exchange.getRequestHeaders().getFirst("X-Telegram-Bot-Api-Secret-Token"),
                exchange.getRequestHeaders().getFirst("Content-Type"),
                body
        );

        respond(exchange, status);
    }

    private byte[] readBody(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[16384];
        int totalBytes = 0;
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            totalBytes += bytesRead;

            if (totalBytes > WebhookReceiver.MAX_REQUEST_BODY_SIZE) {
                throw new RequestBodyTooLargeException();
            }

            outputStream.write(buffer, 0, bytesRead);
        }

        return outputStream.toByteArray();
    }

    private void respond(HttpExchange exchange, int status) throws IOException {
        exchange.sendResponseHeaders(status, -1);

        try (OutputStream output = exchange.getResponseBody()) {
            output.flush();
        }
    }
}