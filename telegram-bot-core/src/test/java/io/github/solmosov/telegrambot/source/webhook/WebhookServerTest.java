package io.github.solmosov.telegrambot.source.webhook;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.context.BotContext;
import io.github.solmosov.telegrambot.dispatcher.Dispatcher;
import io.github.solmosov.telegrambot.exception.GlobalExceptionHandler;
import io.github.solmosov.telegrambot.executor.UpdateExecutor;
import io.github.solmosov.telegrambot.model.Update;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.RejectedExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class WebhookServerTest {

    private WebhookReceiver receiver;

    private WebhookServer webhookServer;

    private int port;

    private static final String SECRET = "test-secret";
    private static final String PATH = "/webhook";

    @BeforeEach
    void setUp() throws IOException {
        receiver = mock(WebhookReceiver.class);

        when(receiver.path()).thenReturn(PATH);
        when(receiver.receive(any(), any(), any())).thenReturn(200);

        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
        }

        webhookServer = new WebhookServer("127.0.0.1", port, receiver);

        webhookServer.start();
    }

    @AfterEach
    void tearDown() {
        webhookServer.stop();
    }

    @Test
    void shouldReturn405WhenMethodIsNotPost() throws Exception {
        HttpURLConnection connection = openConnection("GET");

        assertEquals(405, connection.getResponseCode());
        verify(receiver, never()).receive(any(), any(), any());
    }


    @Test
    void shouldReturn400WhenContentLengthIsInvalid() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        OutputStream outputStream = mock(OutputStream.class);

        Headers headers = new Headers();
        headers.set("Content-Length", "invalid");

        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestHeaders()).thenReturn(headers);
        when(exchange.getResponseBody()).thenReturn(outputStream);

        Method handleMethod = WebhookServer.class
                .getDeclaredMethod("handle", HttpExchange.class);
        handleMethod.setAccessible(true);
        handleMethod.invoke(webhookServer, exchange);

        verify(exchange).sendResponseHeaders(400, -1);
        verify(receiver, never()).receive(any(), any(), any());
    }

    @Test
    void shouldReturn413WhenContentLengthIsTooLarge() throws Exception {
        HttpURLConnection connection = openConnection("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        byte[] body = new byte[WebhookReceiver.MAX_REQUEST_BODY_SIZE + 1];
        connection.setFixedLengthStreamingMode(body.length);

        try (OutputStream output = connection.getOutputStream()) {
            output.write(body);
        }

        assertEquals(413, connection.getResponseCode());
        verify(receiver, never()).receive(any(), any(), any());
    }

    @Test
    void shouldReturn413WhenChunkedBodyIsTooLarge() throws Exception {
        HttpURLConnection connection = openConnection("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setChunkedStreamingMode(8192);

        byte[] body = new byte[WebhookReceiver.MAX_REQUEST_BODY_SIZE + 1];

        try (OutputStream output = connection.getOutputStream()) {
            output.write(body);
        }

        assertEquals(413, connection.getResponseCode());
        verify(receiver, never()).receive(any(), any(), any());
    }

    @Test
    void shouldDelegateHeadersAndBodyToReceiver() throws Exception {
        HttpURLConnection connection = openConnection("POST");
        connection.setRequestProperty("X-Telegram-Bot-Api-Secret-Token", SECRET);
        connection.setRequestProperty("Content-Type", "application/json");

        writeBody(connection, "{\"update_id\":1}");

        assertEquals(200, connection.getResponseCode());

        ArgumentCaptor<byte[]> bodyCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(receiver).receive(eq(SECRET), eq("application/json"), bodyCaptor.capture());

        assertEquals("{\"update_id\":1}",
                new String(bodyCaptor.getValue(), StandardCharsets.UTF_8));
    }

    @Test
    void shouldPassNullHeadersToReceiverWhenMissing() throws Exception {
        HttpURLConnection connection = openConnection("POST");
        writeBody(connection, "{}");
        connection.getResponseCode();

        verify(receiver).receive(isNull(), any(), any());
    }


    @ParameterizedTest
    @ValueSource(ints = {200, 400, 403, 415, 503})
    void shouldReturnStatusProducedByReceiver(int status) throws Exception {
        when(receiver.receive(any(), any(), any())).thenReturn(status);

        HttpURLConnection connection = openConnection("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        writeBody(connection, "{}");

        assertEquals(status, connection.getResponseCode());
    }

    private HttpURLConnection openConnection(String method) throws IOException {
        URL url = new URL("http://127.0.0.1:" + port + PATH);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(3000);
        return connection;
    }

    private void writeBody(HttpURLConnection connection, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        connection.setFixedLengthStreamingMode(bytes.length);
        try (OutputStream output = connection.getOutputStream()) {
            output.write(bytes);
        }
    }

    private void sendPost(String body) throws IOException {
        HttpURLConnection connection = openConnection("POST");

        connection.setRequestProperty(
                "X-Telegram-Bot-Api-Secret-Token",
                SECRET
        );

        connection.setRequestProperty(
                "Content-Type",
                "application/json"
        );

        writeBody(connection, body);

        assertEquals(200, connection.getResponseCode());

        connection.disconnect();
    }
}