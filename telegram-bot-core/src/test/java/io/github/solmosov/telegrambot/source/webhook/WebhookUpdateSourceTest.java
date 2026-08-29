package io.github.solmosov.telegrambot.source.webhook;

import io.github.solmosov.telegrambot.bot.ExecutionMode;
import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.dispatcher.Dispatcher;
import io.github.solmosov.telegrambot.exception.GlobalExceptionHandler;
import io.github.solmosov.telegrambot.exception.api.TelegramApiException;
import io.github.solmosov.telegrambot.exception.api.WebhookSetupException;
import io.github.solmosov.telegrambot.executor.MultiVirtualThreadUpdateExecutor;
import io.github.solmosov.telegrambot.executor.SingleThreadUpdateExecutor;
import io.github.solmosov.telegrambot.json.ObjectMapperFactory;
import io.github.solmosov.telegrambot.source.UpdateSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebhookUpdateSourceTest {

    private static final String BOT_NAME = "test_bot";
    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static final String PATH = "/webhook";
    private static final String URL = "https://example.com";
    private static final String PATH_SECRET = "path-secret";
    private static final String SECRET = "telegram-secret";
    private static final long TIMEOUT = 5000L;

    private TelegramClient client;
    private Dispatcher dispatcher;
    private JsonMapper jsonMapper;
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        client = mock(TelegramClient.class);
        dispatcher = mock(Dispatcher.class);
        jsonMapper = ObjectMapperFactory.create();
        exceptionHandler = mock(GlobalExceptionHandler.class);
    }


    @Test
    void shouldCreateSingleThreadExecutor() {
        try (
                MockedConstruction<SingleThreadUpdateExecutor> executor =
                        mockConstruction(SingleThreadUpdateExecutor.class);
                MockedConstruction<WebhookServer> server =
                        mockConstruction(WebhookServer.class)
        ) {
            create(ExecutionMode.SINGLE_THREAD);

            assertEquals(1, executor.constructed().size());
            assertEquals(1, server.constructed().size());
        }
    }

    @Test
    void shouldCreateMultiVirtualThreadExecutor() {
        try (
                MockedConstruction<MultiVirtualThreadUpdateExecutor> executor =
                        mockConstruction(MultiVirtualThreadUpdateExecutor.class);
                MockedConstruction<WebhookServer> server =
                        mockConstruction(WebhookServer.class)
        ) {
            create(ExecutionMode.MULTI_VIRTUAL_THREAD);

            assertEquals(1, executor.constructed().size());
            assertEquals(1, server.constructed().size());
        }
    }

    @Test
    void shouldSetWebhookAndStartServer() {
        when(client.setWebhook(
                "https://example.com/webhook/test_bot/path-secret",
                SECRET
        )).thenReturn(null);

        try (
                MockedConstruction<SingleThreadUpdateExecutor> executor =
                        mockConstruction(SingleThreadUpdateExecutor.class);
                MockedConstruction<WebhookServer> server =
                        mockConstruction(WebhookServer.class)
        ) {
            WebhookUpdateSource source = create(ExecutionMode.SINGLE_THREAD);

            source.start();

            verify(client).setWebhook(
                    "https://example.com/webhook/test_bot/path-secret",
                    SECRET
            );

            WebhookServer serverMock = server.constructed().getFirst();

            verify(serverMock).start();
        }
    }

    @Test
    void shouldUseNormalizedWebhookUrl() {
        when(client.setWebhook(
                "https://example.com/webhook/test_bot/path-secret",
                SECRET
        )).thenReturn(null);

        try (
                MockedConstruction<SingleThreadUpdateExecutor> executor =
                        mockConstruction(SingleThreadUpdateExecutor.class);
                MockedConstruction<WebhookServer> server =
                        mockConstruction(WebhookServer.class)
        ) {
            WebhookUpdateSource source = create(
                    "https://example.com/",
                    "/webhook/",
                    "test_bot/",
                    "/path-secret",
                    ExecutionMode.SINGLE_THREAD
            );

            source.start();

            verify(client).setWebhook(
                    "https://example.com/webhook/test_bot/path-secret",
                    SECRET
            );
        }
    }

    @Test
    void shouldThrowWebhookSetupExceptionWhenTelegramApiFails() {
        TelegramApiException telegramException =
                new TelegramApiException(400, "Bad Request");

        when(client.setWebhook(
                "https://example.com/webhook/test_bot/path-secret",
                SECRET
        )).thenThrow(telegramException);

        try (
                MockedConstruction<SingleThreadUpdateExecutor> executor =
                        mockConstruction(SingleThreadUpdateExecutor.class);
                MockedConstruction<WebhookServer> server =
                        mockConstruction(WebhookServer.class)
        ) {
            WebhookUpdateSource source = create(
                    ExecutionMode.SINGLE_THREAD
            );

            WebhookSetupException exception = assertThrows(
                    WebhookSetupException.class,
                    source::start
            );

            assertEquals(
                    telegramException.getErrorCode(),
                    exception.getErrorCode()
            );

            verify(client).setWebhook(
                    "https://example.com/webhook/test_bot/path-secret",
                    SECRET
            );

            verify(server.constructed().getFirst(), never()).start();
        }
    }

    @Test
    void shouldNotThrowWhenSetWebhookFailsWithUnexpectedException() {
        when(client.setWebhook(
                "https://example.com/webhook/test_bot/path-secret",
                SECRET
        )).thenThrow(new RuntimeException("Connection failed"));

        try (
                MockedConstruction<SingleThreadUpdateExecutor> executor =
                        mockConstruction(SingleThreadUpdateExecutor.class);
                MockedConstruction<WebhookServer> server =
                        mockConstruction(WebhookServer.class)
        ) {
            WebhookUpdateSource source = create(
                    ExecutionMode.SINGLE_THREAD
            );

            assertDoesNotThrow(source::start);

            verify(client).setWebhook(
                    "https://example.com/webhook/test_bot/path-secret",
                    SECRET
            );

            verify(server.constructed().getFirst()).start();
        }
    }

    @Test
    void shouldStopServer() {
        try (
                MockedConstruction<SingleThreadUpdateExecutor> executor =
                        mockConstruction(SingleThreadUpdateExecutor.class);
                MockedConstruction<WebhookServer> server =
                        mockConstruction(WebhookServer.class)
        ) {
            WebhookUpdateSource source = create(
                    ExecutionMode.SINGLE_THREAD
            );

            source.stop();

            verify(server.constructed().getFirst()).stop();
        }
    }

    @Test
    void shouldShutdownServerAndExecutor() {
        try (
                MockedConstruction<SingleThreadUpdateExecutor> executor =
                        mockConstruction(SingleThreadUpdateExecutor.class);
                MockedConstruction<WebhookServer> server =
                        mockConstruction(WebhookServer.class)
        ) {
            WebhookUpdateSource source = create(
                    ExecutionMode.SINGLE_THREAD
            );

            source.shutdown();

            verify(server.constructed().getFirst()).shutdown();
            verify(executor.constructed().getFirst()).shutdown();
        }
    }

    @Test
    void shouldReturnWebhookSourceType() {
        try (
                MockedConstruction<SingleThreadUpdateExecutor> executor =
                        mockConstruction(SingleThreadUpdateExecutor.class);
                MockedConstruction<WebhookServer> server =
                        mockConstruction(WebhookServer.class)
        ) {
            WebhookUpdateSource source = create(
                    ExecutionMode.SINGLE_THREAD
            );

            assertEquals(
                    UpdateSource.SourceType.WEBHOOK,
                    source.getSourceType()
            );
        }
    }

    private WebhookUpdateSource create(ExecutionMode executionMode) {
        return create(
                URL,
                PATH,
                BOT_NAME,
                PATH_SECRET,
                executionMode
        );
    }

    private WebhookUpdateSource create(
            String url,
            String path,
            String botName,
            String pathSecret,
            ExecutionMode executionMode
    ) {
        return new WebhookUpdateSource(
                botName,
                client,
                dispatcher,
                executionMode,
                jsonMapper,

                HOST,
                PORT,
                path,
                url,
                pathSecret,
                SECRET,

                exceptionHandler,
                TIMEOUT
        );
    }

}