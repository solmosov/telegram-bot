package io.github.solmosov.telegrambot.source.webhook;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.context.BotContext;
import io.github.solmosov.telegrambot.dispatcher.Dispatcher;
import io.github.solmosov.telegrambot.exception.GlobalExceptionHandler;
import io.github.solmosov.telegrambot.executor.UpdateExecutor;
import io.github.solmosov.telegrambot.model.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.RejectedExecutionException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class WebhookReceiverTest {

    private static final String SECRET = "test-secret";
    private static final String PATH = "/webhook";
    private static final String JSON = "application/json";

    private UpdateExecutor updateExecutor;
    private Dispatcher dispatcher;
    private TelegramClient telegramClient;
    private GlobalExceptionHandler globalExceptionHandler;
    private JsonMapper jsonMapper;

    private WebhookReceiver receiver;

    @BeforeEach
    void setUp() {
        updateExecutor = mock(UpdateExecutor.class);
        dispatcher = mock(Dispatcher.class);
        telegramClient = mock(TelegramClient.class);
        globalExceptionHandler = mock(GlobalExceptionHandler.class);

        // If your Update model already uses @JsonProperty / @JsonNaming,
        // the naming strategy line can be removed.
        jsonMapper = JsonMapper.builder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();

        receiver = new WebhookReceiver(
                "test-bot",
                PATH,
                SECRET,
                updateExecutor,
                dispatcher,
                jsonMapper,
                telegramClient,
                globalExceptionHandler
        );
    }

    // ---------- constructor / lifecycle ----------

    @Test
    void shouldExposeConfiguredPath() {
        assertEquals(PATH, receiver.path());
    }

    @Test
    void shouldThrowWhenSecretIsNull() {
        assertThrows(IllegalArgumentException.class, () -> newReceiver(null));
    }

    @Test
    void shouldThrowWhenSecretIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> newReceiver("   "));
    }

    @Test
    void shouldStartAndShutdownWithoutErrors() {
        assertDoesNotThrow(() -> {
            receiver.start();
            receiver.shutdown();
        });
    }

    @Test
    void shouldShutdownWithoutStart() {
        assertDoesNotThrow(() -> receiver.shutdown());
    }

    // ---------- secret ----------

    @Test
    void shouldReturn403WhenSecretIsMissing() {
        int status = receiver.receive(null, JSON, bytes("{}"));

        assertEquals(403, status);
        verifyNoInteractions(updateExecutor);
    }

    @Test
    void shouldReturn403WhenSecretIsBlank() {
        int status = receiver.receive("  ", JSON, bytes("{}"));

        assertEquals(403, status);
        verifyNoInteractions(updateExecutor);
    }

    @Test
    void shouldReturn403WhenSecretIsInvalid() {
        int status = receiver.receive("wrong-secret", JSON, bytes("{}"));

        assertEquals(403, status);
        verifyNoInteractions(updateExecutor);
    }

    @Test
    void shouldCheckSecretBeforeContentType() {
        int status = receiver.receive("wrong-secret", "text/plain", bytes("{}"));

        assertEquals(403, status);
    }

    // ---------- content type ----------

    @Test
    void shouldReturn415WhenContentTypeIsNull() {
        int status = receiver.receive(SECRET, null, bytes("{}"));

        assertEquals(415, status);
        verifyNoInteractions(updateExecutor);
    }

    @Test
    void shouldReturn415WhenContentTypeIsInvalid() {
        int status = receiver.receive(SECRET, "text/plain", bytes("{}"));

        assertEquals(415, status);
        verifyNoInteractions(updateExecutor);
    }

    @Test
    void shouldAcceptContentTypeWithCharsetAndDifferentCase() {
        int status = receiver.receive(
                SECRET,
                "Application/JSON; charset=utf-8",
                bytes(updateJson(1))
        );

        assertEquals(200, status);
    }

    // ---------- body ----------

    @Test
    void shouldReturn400WhenBodyIsNull() {
        assertEquals(400, receiver.receive(SECRET, JSON, null));
        verifyNoInteractions(updateExecutor);
    }

    @Test
    void shouldReturn400WhenBodyIsEmpty() {
        assertEquals(400, receiver.receive(SECRET, JSON, new byte[0]));
        verifyNoInteractions(updateExecutor);
    }

    @Test
    void shouldReturn413WhenBodyIsTooLarge() {
        byte[] body = new byte[WebhookReceiver.MAX_REQUEST_BODY_SIZE + 1];

        assertEquals(413, receiver.receive(SECRET, JSON, body));
        verifyNoInteractions(updateExecutor);
    }

    @Test
    void shouldReturn400WhenJsonIsInvalid() {
        int status = receiver.receive(SECRET, JSON, bytes("{invalid-json"));

        assertEquals(400, status);
        verifyNoInteractions(updateExecutor);
    }

    // ---------- submit ----------

    @Test
    void shouldAcceptValidUpdate() {
        int status = receiver.receive(SECRET, JSON, bytes(updateJson(12345)));

        assertEquals(200, status);
        verify(updateExecutor).submit(eq(12345L), any(Runnable.class));
    }

    @Test
    void shouldUseMessageChatIdAsExecutorKey() {
        String json = """
                {
                  "update_id": 12345,
                  "message": {
                    "message_id": 1,
                    "date": 1700000000,
                    "chat": { "id": 999, "type": "private" },
                    "text": "hello"
                  }
                }
                """;

        assertEquals(200, receiver.receive(SECRET, JSON, bytes(json)));
        verify(updateExecutor).submit(eq(999L), any(Runnable.class));
    }

    @Test
    void shouldUseCallbackFromUserIdAsExecutorKey() {
        assertEquals(200, receiver.receive(SECRET, JSON, bytes(callbackJson(12345, 777))));
        verify(updateExecutor).submit(eq(777L), any(Runnable.class));
    }

    @Test
    void shouldUseUpdateIdAsExecutorKeyWhenNoMessageOrCallback() {
        assertEquals(200, receiver.receive(SECRET, JSON, bytes(updateJson(555))));
        verify(updateExecutor).submit(eq(555L), any(Runnable.class));
    }

    // ---------- deduplication ----------

    @Test
    void shouldNotSubmitDuplicateUpdate() {
        byte[] body = bytes(updateJson(12345));

        assertEquals(200, receiver.receive(SECRET, JSON, body));
        assertEquals(200, receiver.receive(SECRET, JSON, body));

        verify(updateExecutor, times(1)).submit(eq(12345L), any(Runnable.class));
    }

    @Test
    void shouldSubmitDifferentUpdatesSeparately() {
        receiver.receive(SECRET, JSON, bytes(updateJson(1)));
        receiver.receive(SECRET, JSON, bytes(updateJson(2)));

        verify(updateExecutor).submit(eq(1L), any(Runnable.class));
        verify(updateExecutor).submit(eq(2L), any(Runnable.class));
    }

    // ---------- executor rejection ----------

    @Test
    void shouldReturn503WhenExecutorRejectsUpdate() {
        doThrow(new RejectedExecutionException())
                .when(updateExecutor)
                .submit(anyLong(), any(Runnable.class));

        int status = receiver.receive(SECRET, JSON, bytes(updateJson(12345)));

        assertEquals(503, status);
    }

    @Test
    void shouldAllowRetryAfterExecutorRejection() {
        doThrow(new RejectedExecutionException())
                .doNothing()
                .when(updateExecutor)
                .submit(anyLong(), any(Runnable.class));

        byte[] body = bytes(updateJson(12345));

        assertEquals(503, receiver.receive(SECRET, JSON, body));
        assertEquals(200, receiver.receive(SECRET, JSON, body));

        verify(updateExecutor, times(2)).submit(eq(12345L), any(Runnable.class));
    }

    // ---------- processing ----------

    @Test
    void shouldExecuteSubmittedRunnable() throws Exception {
        receiver.receive(SECRET, JSON, bytes(callbackJson(12345, 777)));

        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        verify(updateExecutor).submit(eq(777L), captor.capture());

        captor.getValue().run();

        verify(dispatcher).dispatch(any(Update.class), any(BotContext.class));
        verifyNoInteractions(globalExceptionHandler);
    }

    @Test
    void shouldHandleDispatcherException() throws Exception {
        doThrow(new RuntimeException("test error"))
                .when(dispatcher)
                .dispatch(any(Update.class), any(BotContext.class));

        String json = """
                {
                  "update_id": 12345,
                  "message": {
                    "message_id": 1,
                    "chat": { "id": 12345, "type": "private" },
                    "date": 1700000000,
                    "text": "hello"
                  }
                }
                """;

        receiver.receive(SECRET, JSON, bytes(json));

        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        verify(updateExecutor).submit(eq(12345L), captor.capture());

        captor.getValue().run();

        verify(globalExceptionHandler).handle(
                any(RuntimeException.class),
                any(Update.class),
                any(BotContext.class)
        );
    }

    // ---------- helpers ----------

    private WebhookReceiver newReceiver(String secret) {
        return new WebhookReceiver(
                "test-bot", PATH, secret,
                updateExecutor, dispatcher, jsonMapper,
                telegramClient, globalExceptionHandler
        );
    }

    private static byte[] bytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    private static String updateJson(long updateId) {
        return "{\"update_id\": " + updateId + "}";
    }

    private static String callbackJson(long updateId, long userId) {
        return """
                {
                  "update_id": %d,
                  "callback_query": {
                    "id": "callback-1",
                    "from": { "id": %d, "is_bot": false, "first_name": "Test" },
                    "message": {
                      "message_id": 1,
                      "date": 1700000000,
                      "chat": { "id": %d, "type": "private" }
                    }
                  }
                }
                """.formatted(updateId, userId, userId);
    }
}