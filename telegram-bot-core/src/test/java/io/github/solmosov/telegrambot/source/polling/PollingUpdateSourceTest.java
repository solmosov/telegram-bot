package io.github.solmosov.telegrambot.source.polling;

import io.github.solmosov.telegrambot.bot.ExecutionMode;
import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.context.BotContext;
import io.github.solmosov.telegrambot.dispatcher.Dispatcher;
import io.github.solmosov.telegrambot.exception.GlobalExceptionHandler;
import io.github.solmosov.telegrambot.exception.client.TelegramClientException;
import io.github.solmosov.telegrambot.model.*;
import io.github.solmosov.telegrambot.model.*;
import io.github.solmosov.telegrambot.source.UpdateSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PollingUpdateSourceTest {

    private TelegramClient client;
    private Dispatcher dispatcher;
    private GlobalExceptionHandler globalExceptionHandler;

    private PollingUpdateSource source;

    @BeforeEach
    void setUp() {
        client = mock(TelegramClient.class);
        dispatcher = mock(Dispatcher.class);
        globalExceptionHandler = mock(GlobalExceptionHandler.class);
    }

    @AfterEach
    void tearDown() {
        if (source != null) {
            source.shutdown();
        }
    }

    @Test
    void shouldReturnPollingSourceType() {
        source = createSource();

        assertEquals(
                UpdateSource.SourceType.POLLING,
                source.getSourceType()
        );
    }

    @Test
    void shouldNotDeleteWebhookWhenWebhookUrlIsEmpty() {
        source = createSource();

        WebhookInfo webhookInfo = new WebhookInfo(
                "",
                false,
                0,
                null,
                null,
                null,
                null
        );

        when(client.getWebhookInfo())
                .thenReturn(new TelegramResponse<>(
                        true,
                        webhookInfo,
                        null,
                        null
                ));

        when(client.getUpdates(0))
                .thenReturn(new TelegramResponse<>(
                        true,
                        List.of(),
                        null,
                        null
                ));

        source.start();

        verify(client, timeout(1000))
                .getWebhookInfo();

        verify(client, never())
                .deleteWebhook();
    }

    @Test
    void shouldDeleteWebhookWhenWebhookUrlExists() {
        source = createSource();

        WebhookInfo webhookInfo = new WebhookInfo(
                "https://example.com/webhook",
                false,
                0,
                null,
                null,
                null,
                null
        );

        when(client.getWebhookInfo())
                .thenReturn(new TelegramResponse<>(
                        true,
                        webhookInfo,
                        null,
                        null
                ));

        when(client.deleteWebhook())
                .thenReturn(new TelegramResponse<>(
                        true,
                        true,
                        null,
                        null
                ));

        when(client.getUpdates(0))
                .thenReturn(new TelegramResponse<>(
                        true,
                        List.of(),
                        null,
                        null
                ));

        source.start();

        verify(client, timeout(1000))
                .getWebhookInfo();

        verify(client, timeout(1000))
                .deleteWebhook();
    }

    @Test
    void shouldThrowExceptionWhenWebhookDeletionFails() {
        source = createSource();

        WebhookInfo webhookInfo = new WebhookInfo(
                "https://example.com/webhook",
                false,
                0,
                null,
                null,
                null,
                null
        );

        when(client.getWebhookInfo())
                .thenReturn(new TelegramResponse<>(
                        true,
                        webhookInfo,
                        null,
                        null
                ));

        when(client.deleteWebhook())
                .thenReturn(new TelegramResponse<>(
                        false,
                        false,
                        400,
                        "Failed to delete webhook"
                ));

        assertThrows(
                IllegalStateException.class,
                () -> source.start()
        );

        verify(client).getWebhookInfo();
        verify(client).deleteWebhook();
        verify(client, never()).getUpdates(anyLong());
    }

    @Test
    void shouldStartPollingThread() {
        source = createSource();

        mockWebhookDoesNotExist();
        mockEmptyUpdates();

        source.start();

        assertNotNull(source.getPollingThread());
        assertTrue(source.getPollingThread().isAlive());
    }

    @Test
    void shouldPollUpdates() {
        source = createSource();

        mockWebhookDoesNotExist();
        mockEmptyUpdates();

        source.start();

        verify(client, timeout(1000).atLeastOnce())
                .getUpdates(0L);
    }

    @Test
    void shouldUpdateOffsetAfterReceivingUpdate() throws InterruptedException {
        source = createSource();

        mockWebhookDoesNotExist();

        Update update = mock(Update.class);

        when(update.updateId())
                .thenReturn(10L);

        when(client.getUpdates(0L))
                .thenReturn(new TelegramResponse<>(
                        true,
                        List.of(update),
                        null,
                        null
                ));

        when(client.getUpdates(11L))
                .thenAnswer(invocation -> {
                    source.stop();

                    return new TelegramResponse<>(
                            true,
                            List.of(),
                            null,
                            null
                    );
                });

        source.start();

        verify(client, timeout(1000))
                .getUpdates(0L);

        verify(client, timeout(1000))
                .getUpdates(11L);

        source.getPollingThread().join(1000);
    }

    @Test
    void shouldDispatchReceivedUpdate() throws InterruptedException {
        source = createSource();

        mockWebhookDoesNotExist();

        Chat chat = new Chat(
                123L,
                "John",
                "Doe",
                null,
                "john",
                ChatType.PRIVATE
        );

        Message message = new Message(
                1L,
                null,
                chat,
                123456789L,
                "Hello",
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Update update = new Update(
                10L,
                message,
                null
        );

        CountDownLatch dispatched = new CountDownLatch(1);

        doAnswer(invocation -> {
            dispatched.countDown();
            return null;
        }).when(dispatcher).dispatch(
                eq(update),
                any(BotContext.class)
        );

        when(client.getUpdates(0L))
                .thenReturn(new TelegramResponse<>(
                        true,
                        List.of(update),
                        null,
                        null
                ));

        when(client.getUpdates(11L))
                .thenAnswer(invocation -> {
                    source.stop();

                    return new TelegramResponse<>(
                            true,
                            List.of(),
                            null,
                            null
                    );
                });

        source.start();

        assertTrue(
                dispatched.await(2, TimeUnit.SECONDS),
                "Update was not dispatched"
        );

        verify(dispatcher).dispatch(
                eq(update),
                any(BotContext.class)
        );

        source.getPollingThread().join(1000);
    }

    @Test
    void shouldStopPolling() throws InterruptedException {
        source = createSource();

        mockWebhookDoesNotExist();
        mockEmptyUpdates();

        source.start();

        Thread pollingThread = source.getPollingThread();

        assertTrue(pollingThread.isAlive());

        source.stop();
        source.shutdown();

        pollingThread.join(1000);

        assertFalse(pollingThread.isAlive());
    }

    @Test
    void shouldUseSingleThreadExecutionMode() {
        source = createSource(ExecutionMode.SINGLE_THREAD);

        assertNotNull(source);
    }

    @Test
    void shouldUseMultiVirtualThreadExecutionMode() {
        source = createSource(ExecutionMode.MULTI_VIRTUAL_THREAD);

        assertNotNull(source);
    }

    @Test
    void shouldRetryPollingAfterTelegramClientException()
            throws InterruptedException {

        source = createSource();

        mockWebhookDoesNotExist();

        when(client.getUpdates(0L))
                .thenThrow(mock(TelegramClientException.class))
                .thenReturn(new TelegramResponse<>(
                        true,
                        List.of(),
                        null,
                        null
                ));

        source.start();

        verify(client, timeout(2500).atLeast(2))
                .getUpdates(0L);

        source.stop();
        source.shutdown();
    }

    @Test
    void shouldShutdownExecutorWhenShutdownIsCalled() {
        source = createSource();

        assertDoesNotThrow(() -> source.shutdown());
    }

    private void mockWebhookDoesNotExist() {
        WebhookInfo webhookInfo = new WebhookInfo(
                "",
                false,
                0,
                null,
                null,
                null,
                null
        );

        when(client.getWebhookInfo())
                .thenReturn(new TelegramResponse<>(
                        true,
                        webhookInfo,
                        null,
                        null
                ));
    }

    private void mockEmptyUpdates() {
        when(client.getUpdates(anyLong()))
                .thenReturn(new TelegramResponse<>(
                        true,
                        List.of(),
                        null,
                        null
                ));
    }

    private PollingUpdateSource createSource() {
        return createSource(ExecutionMode.SINGLE_THREAD);
    }

    private PollingUpdateSource createSource(
            ExecutionMode executionMode
    ) {
        return new PollingUpdateSource(
                "testBot",
                client,
                dispatcher,
                executionMode,
                globalExceptionHandler,
                1000
        );
    }
}