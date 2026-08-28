package io.github.shahbozolmosov.telegrambot.source.webhook;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.context.BotContext;
import io.github.shahbozolmosov.telegrambot.dispatcher.Dispatcher;
import io.github.shahbozolmosov.telegrambot.exception.GlobalExceptionHandler;
import io.github.shahbozolmosov.telegrambot.executor.UpdateExecutor;
import io.github.shahbozolmosov.telegrambot.model.Update;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private UpdateExecutor updateExecutor;
    private Dispatcher dispatcher;
    private TelegramClient telegramClient;
    private GlobalExceptionHandler globalExceptionHandler;
    private JsonMapper jsonMapper;

    private WebhookServer webhookServer;

    private int port;

    private static final String SECRET = "test-secret";
    private static final String PATH = "/webhook";

    @BeforeEach
    void setUp() throws IOException {
        updateExecutor = mock(UpdateExecutor.class);
        dispatcher = mock(Dispatcher.class);
        telegramClient = mock(TelegramClient.class);
        globalExceptionHandler = mock(GlobalExceptionHandler.class);

        jsonMapper = JsonMapper.builder().build();

        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
        }

        webhookServer = new WebhookServer(
                "test-bot",
                "127.0.0.1",
                port,
                PATH,
                SECRET,
                updateExecutor,
                dispatcher,
                jsonMapper,
                telegramClient,
                globalExceptionHandler
        );

        webhookServer.start();
    }

    @AfterEach
    void tearDown() {
        webhookServer.stop();
    }

    @Test
    void shouldReturn405WhenMethodIsNotPost() throws Exception {
        HttpURLConnection connection = openConnection("GET");

        int status = connection.getResponseCode();

        assertEquals(405, status);

        verifyNoInteractions(updateExecutor);
    }

    @Test
    void shouldReturn403WhenSecretIsMissing() throws Exception {
        HttpURLConnection connection = openConnection("POST");

        connection.setRequestProperty(
                "Content-Type",
                "application/json"
        );

        writeBody(connection, "{}");

        int status = connection.getResponseCode();

        assertEquals(403, status);

        verifyNoInteractions(updateExecutor);
    }

    @Test
    void shouldReturn403WhenSecretIsInvalid() throws Exception {
        HttpURLConnection connection = openConnection("POST");

        connection.setRequestProperty(
                "X-Telegram-Bot-Api-Secret-Token",
                "wrong-secret"
        );

        connection.setRequestProperty(
                "Content-Type",
                "application/json"
        );

        writeBody(connection, "{}");

        int status = connection.getResponseCode();

        assertEquals(403, status);

        verifyNoInteractions(updateExecutor);
    }

    @Test
    void shouldReturn415WhenContentTypeIsInvalid() throws Exception {
        HttpURLConnection connection = openConnection("POST");

        connection.setRequestProperty(
                "X-Telegram-Bot-Api-Secret-Token",
                SECRET
        );

        connection.setRequestProperty(
                "Content-Type",
                "text/plain"
        );

        writeBody(connection, "{}");

        int status = connection.getResponseCode();

        assertEquals(415, status);

        verifyNoInteractions(updateExecutor);
    }

    @Test
    void shouldReturn400WhenContentLengthIsInvalid() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        OutputStream outputStream = mock(OutputStream.class);

        Headers headers = new Headers();

        headers.set(
                "X-Telegram-Bot-Api-Secret-Token",
                SECRET
        );

        headers.set(
                "Content-Type",
                "application/json"
        );

        headers.set(
                "Content-Length",
                "invalid"
        );

        when(exchange.getRequestMethod())
                .thenReturn("POST");

        when(exchange.getRequestHeaders())
                .thenReturn(headers);

        when(exchange.getResponseBody())
                .thenReturn(outputStream);

        Method handleMethod = WebhookServer.class
                .getDeclaredMethod("handle", HttpExchange.class);

        handleMethod.setAccessible(true);

        handleMethod.invoke(webhookServer, exchange);

        verify(exchange)
                .sendResponseHeaders(400, 0);

        verify(outputStream)
                .write(new byte[0]);

        verifyNoInteractions(updateExecutor);
    }

    @Test
    void shouldReturn413WhenRequestBodyIsTooLarge() throws Exception {
        HttpURLConnection connection = openConnection("POST");

        connection.setRequestProperty(
                "X-Telegram-Bot-Api-Secret-Token",
                SECRET
        );

        connection.setRequestProperty(
                "Content-Type",
                "application/json"
        );

        byte[] body = new byte[256 * 1024 + 1];

        connection.setFixedLengthStreamingMode(body.length);

        try (OutputStream output = connection.getOutputStream()) {
            output.write(body);
        }

        int status = connection.getResponseCode();

        assertEquals(413, status);

        verifyNoInteractions(updateExecutor);
    }

    @Test
    void shouldReturn400WhenJsonIsInvalid() throws Exception {
        HttpURLConnection connection = openConnection("POST");

        connection.setRequestProperty(
                "X-Telegram-Bot-Api-Secret-Token",
                SECRET
        );

        connection.setRequestProperty(
                "Content-Type",
                "application/json"
        );

        writeBody(connection, "{invalid-json");

        int status = connection.getResponseCode();

        assertEquals(400, status);

        verifyNoInteractions(updateExecutor);
    }

    @Test
    void shouldAcceptValidUpdate() throws Exception {
        String json = """
                {
                  "update_id": 12345
                }
                """;

        HttpURLConnection connection = openConnection("POST");

        connection.setRequestProperty(
                "X-Telegram-Bot-Api-Secret-Token",
                SECRET
        );

        connection.setRequestProperty(
                "Content-Type",
                "application/json"
        );

        writeBody(connection, json);

        int status = connection.getResponseCode();

        assertEquals(200, status);

        verify(updateExecutor).submit(
                eq(12345L),
                any(Runnable.class)
        );
    }

    @Test
    void shouldReturn503WhenExecutorRejectsUpdate() throws Exception {
        doThrow(new RejectedExecutionException())
                .when(updateExecutor)
                .submit(anyLong(), any(Runnable.class));

        String json = """
                {
                  "update_id": 12345
                }
                """;

        HttpURLConnection connection = openConnection("POST");

        connection.setRequestProperty(
                "X-Telegram-Bot-Api-Secret-Token",
                SECRET
        );

        connection.setRequestProperty(
                "Content-Type",
                "application/json"
        );

        writeBody(connection, json);

        int status = connection.getResponseCode();

        assertEquals(503, status);
    }

    @Test
    void shouldUseMessageChatIdAsExecutorKey() throws Exception {
        String json = """
            {
              "update_id": 12345,
              "message": {
                "message_id": 1,
                "date": 1700000000,
                "chat": {
                  "id": 999,
                  "type": "private"
                },
                "text": "hello"
              }
            }
            """;

        HttpURLConnection connection = openConnection("POST");

        connection.setRequestProperty(
                "X-Telegram-Bot-Api-Secret-Token",
                SECRET
        );

        connection.setRequestProperty(
                "Content-Type",
                "application/json"
        );

        writeBody(connection, json);

        assertEquals(200, connection.getResponseCode());

        verify(updateExecutor).submit(
                eq(999L),
                any(Runnable.class)
        );
    }

    @Test
    void shouldUseCallbackFromUserIdAsExecutorKey() throws Exception {
        String json = """
                {
                  "update_id": 12345,
                  "callback_query": {
                    "from": {
                      "id": 777
                    }
                  }
                }
                """;

        HttpURLConnection connection = openConnection("POST");

        connection.setRequestProperty(
                "X-Telegram-Bot-Api-Secret-Token",
                SECRET
        );

        connection.setRequestProperty(
                "Content-Type",
                "application/json"
        );

        writeBody(connection, json);

        assertEquals(200, connection.getResponseCode());

        verify(updateExecutor).submit(
                eq(777L),
                any(Runnable.class)
        );
    }

    @Test
    void shouldNotSubmitDuplicateUpdate() throws Exception {
        String json = """
                {
                  "update_id": 12345
                }
                """;

        sendPost(json);

        sendPost(json);

        verify(
                updateExecutor,
                times(1)
        ).submit(
                eq(12345L),
                any(Runnable.class)
        );
    }

    @Test
    void shouldExecuteSubmittedRunnable() throws Exception {
        String json = """
            {
              "update_id": 12345,
              "callback_query": {
                "id": "callback-1",
                "from": {
                  "id": 777,
                  "is_bot": false,
                  "first_name": "Test"
                },
                "message": {
                  "message_id": 1,
                  "date": 1700000000,
                  "chat": {
                    "id": 777,
                    "type": "private"
                  }
                }
              }
            }
            """;

        sendPost(json);

        ArgumentCaptor<Runnable> captor =
                ArgumentCaptor.forClass(Runnable.class);

        verify(updateExecutor).submit(
                eq(777L),
                captor.capture()
        );

        captor.getValue().run();

        verify(dispatcher).dispatch(
                any(Update.class),
                any(BotContext.class)
        );
    }

    @Test
    void shouldHandleDispatcherException() throws Exception {
        doThrow(new RuntimeException("test error"))
                .when(dispatcher)
                .dispatch(
                        any(Update.class),
                        any(BotContext.class)
                );

        String json = """
            {
              "update_id": 12345,
              "message": {
                "message_id": 1,
                "chat": {
                  "id": 12345,
                  "type": "private"
                },
                "date": 1700000000,
                "text": "hello"
              }
            }
            """;

        sendPost(json);

        ArgumentCaptor<Runnable> captor =
                ArgumentCaptor.forClass(Runnable.class);

        verify(updateExecutor).submit(
                eq(12345L),
                captor.capture()
        );

        captor.getValue().run();

        verify(globalExceptionHandler).handle(
                any(RuntimeException.class),
                any(Update.class),
                any(BotContext.class)
        );
    }

    @Test
    void shouldRequireSecretInConstructor() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new WebhookServer(
                        "test-bot",
                        "127.0.0.1",
                        port,
                        PATH,
                        "",
                        updateExecutor,
                        dispatcher,
                        jsonMapper,
                        telegramClient,
                        globalExceptionHandler
                )
        );
    }

    private HttpURLConnection openConnection(
            String method
    ) throws IOException {

        URL url = new URL(
                "http://127.0.0.1:"
                        + port
                        + PATH
        );

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(3000);

        return connection;
    }

    private void writeBody(
            HttpURLConnection connection,
            String body
    ) throws IOException {

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);

        connection.setFixedLengthStreamingMode(bytes.length);

        try (OutputStream output =
                     connection.getOutputStream()) {

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