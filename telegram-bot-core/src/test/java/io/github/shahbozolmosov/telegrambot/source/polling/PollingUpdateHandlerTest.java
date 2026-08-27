package io.github.shahbozolmosov.telegrambot.source.polling;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.context.BotContext;
import io.github.shahbozolmosov.telegrambot.dispatcher.Dispatcher;
import io.github.shahbozolmosov.telegrambot.exception.GlobalExceptionHandler;
import io.github.shahbozolmosov.telegrambot.executor.UpdateExecutor;
import io.github.shahbozolmosov.telegrambot.model.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.*;

class PollingUpdateHandlerTest {

    private TelegramClient client;
    private Dispatcher dispatcher;
    private UpdateExecutor updateExecutor;
    private GlobalExceptionHandler globalExceptionHandler;

    private PollingUpdateHandler handler;

    @BeforeEach
    void setUp() {
        client = mock(TelegramClient.class);
        dispatcher = mock(Dispatcher.class);
        updateExecutor = mock(UpdateExecutor.class);
        globalExceptionHandler = mock(GlobalExceptionHandler.class);

        handler = new PollingUpdateHandler(
                "testBot",
                client,
                dispatcher,
                updateExecutor,
                globalExceptionHandler
        );
    }

    @Test
    void shouldSubmitUpdateUsingMessageChatId() {
        Update update = mock(Update.class, RETURNS_DEEP_STUBS);

        when(update.message().chat().id()).thenReturn(123L);

        handler.handle(update);

        ArgumentCaptor<Runnable> runnableCaptor =
                ArgumentCaptor.forClass(Runnable.class);

        verify(updateExecutor).submit(eq(123L), runnableCaptor.capture());

        assertEquals(1, runnableCaptor.getAllValues().size());
    }

    @Test
    void shouldSubmitUpdateUsingCallbackQueryChatId() {
        Update update = mock(Update.class, RETURNS_DEEP_STUBS);

        when(update.message()).thenReturn(null);
        when(update.callbackQuery().message().chat().id()).thenReturn(456L);

        handler.handle(update);

        ArgumentCaptor<Runnable> runnableCaptor =
                ArgumentCaptor.forClass(Runnable.class);

        verify(updateExecutor).submit(eq(456L), runnableCaptor.capture());
    }

    @Test
    void shouldUseUpdateIdWhenChatIdCannotBeExtracted() {
        Update update = mock(Update.class);

        when(update.message()).thenReturn(null);
        when(update.callbackQuery()).thenReturn(null);
        when(update.updateId()).thenReturn(999L);

        handler.handle(update);

        verify(updateExecutor).submit(eq(999L), any(Runnable.class));
    }

    @Test
    void shouldDispatchUpdate() {
        Update update = mock(Update.class, RETURNS_DEEP_STUBS);

        when(update.message().chat().id()).thenReturn(123L);
        when(update.updateId()).thenReturn(1L);

        handler.handle(update);

        ArgumentCaptor<Runnable> runnableCaptor =
                ArgumentCaptor.forClass(Runnable.class);

        verify(updateExecutor).submit(eq(123L), runnableCaptor.capture());

        runnableCaptor.getValue().run();

        verify(dispatcher).dispatch(
                eq(update),
                any(BotContext.class)
        );
    }

    @Test
    void shouldHandleDispatcherException() {
        Update update = mock(Update.class, RETURNS_DEEP_STUBS);

        when(update.message().chat().id()).thenReturn(123L);
        when(update.updateId()).thenReturn(1L);

        RuntimeException exception = new RuntimeException("test error");

        doThrow(exception)
                .when(dispatcher)
                .dispatch(eq(update), any(BotContext.class));

        handler.handle(update);

        ArgumentCaptor<Runnable> runnableCaptor =
                ArgumentCaptor.forClass(Runnable.class);

        verify(updateExecutor).submit(eq(123L), runnableCaptor.capture());

        runnableCaptor.getValue().run();

        verify(globalExceptionHandler).handle(
                eq(exception),
                eq(update),
                any(BotContext.class)
        );
    }

    @Test
    void shouldReturnExecutor() {
        assertEquals(updateExecutor, handler.getExecutor());
    }

}