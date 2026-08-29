package io.github.solmosov.telegrambot.dispatcher;

import io.github.solmosov.telegrambot.authorization.AuthorizationDecision;
import io.github.solmosov.telegrambot.authorization.AuthorizationManager;
import io.github.solmosov.telegrambot.context.BotContext;
import io.github.solmosov.telegrambot.exception.authorization.AccessDeniedException;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.model.Update;
import io.github.solmosov.telegrambot.model.UpdateType;
import io.github.solmosov.telegrambot.registry.Registry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DispatcherTest {

    private static final String BOT_NAME = "myBot";

    @Test
    void dispatch_shouldHandleUpdate_whenUpdateHandlerExistsAndAuthorizationGranted() {
        Registry registry = mock(Registry.class);
        Handler handler = mock(Handler.class);
        AuthorizationManager authorizationManager = mock(AuthorizationManager.class);
        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);

        when(registry.getUpdateHandler(BOT_NAME)).thenReturn(handler);
        when(authorizationManager.authorize(context, handler))
                .thenReturn(AuthorizationDecision.granted());

        Dispatcher dispatcher = new Dispatcher(
                BOT_NAME,
                registry,
                List.of(),
                authorizationManager
        );

        dispatcher.dispatch(update, context);

        verify(authorizationManager).authorize(context, handler);
        verify(handler).handle(update, context);
    }

    @Test
    void dispatch_shouldNotHandleUpdate_whenUpdateHandlerDoesNotExist() {
        Registry registry = mock(Registry.class);
        AuthorizationManager authorizationManager = mock(AuthorizationManager.class);
        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);

        when(registry.getUpdateHandler(BOT_NAME)).thenReturn(null);

        Dispatcher dispatcher = new Dispatcher(
                BOT_NAME,
                registry,
                List.of(),
                authorizationManager
        );

        dispatcher.dispatch(update, context);

        verifyNoInteractions(authorizationManager);
    }

    @Test
    void dispatch_shouldThrowAccessDeniedException_whenAuthorizationDenied() {
        Registry registry = mock(Registry.class);
        Handler handler = mock(Handler.class);
        AuthorizationManager authorizationManager = mock(AuthorizationManager.class);
        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);

        when(registry.getUpdateHandler(BOT_NAME)).thenReturn(handler);
        when(authorizationManager.authorize(context, handler))
                .thenReturn(AuthorizationDecision.denied());

        Dispatcher dispatcher = new Dispatcher(
                BOT_NAME,
                registry,
                List.of(),
                authorizationManager
        );

        assertThrows(
                AccessDeniedException.class,
                () -> dispatcher.dispatch(update, context)
        );

        verify(authorizationManager).authorize(context, handler);
        verify(handler, never()).handle(any(), any());
    }

    @Test
    void dispatch_shouldDispatchToSupportedUpdateTypeDispatcher() {
        Registry registry = mock(Registry.class);
        AuthorizationManager authorizationManager = mock(AuthorizationManager.class);
        UpdateTypeDispatcher typeDispatcher = mock(UpdateTypeDispatcher.class);
        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);

        when(registry.getUpdateHandler(BOT_NAME)).thenReturn(null);
        when(update.type()).thenReturn(UpdateType.MESSAGE);
        when(typeDispatcher.supports(UpdateType.MESSAGE)).thenReturn(true);

        Dispatcher dispatcher = new Dispatcher(
                BOT_NAME,
                registry,
                List.of(typeDispatcher),
                authorizationManager
        );

        dispatcher.dispatch(update, context);

        verify(typeDispatcher).supports(UpdateType.MESSAGE);
        verify(typeDispatcher).dispatch(BOT_NAME, update, context);
    }

    @Test
    void dispatch_shouldNotDispatchToUnsupportedUpdateTypeDispatcher() {
        Registry registry = mock(Registry.class);
        AuthorizationManager authorizationManager = mock(AuthorizationManager.class);
        UpdateTypeDispatcher typeDispatcher = mock(UpdateTypeDispatcher.class);
        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);

        when(registry.getUpdateHandler(BOT_NAME)).thenReturn(null);
        when(update.type()).thenReturn(UpdateType.MESSAGE);
        when(typeDispatcher.supports(UpdateType.MESSAGE)).thenReturn(false);

        Dispatcher dispatcher = new Dispatcher(
                BOT_NAME,
                registry,
                List.of(typeDispatcher),
                authorizationManager
        );

        dispatcher.dispatch(update, context);

        verify(typeDispatcher).supports(UpdateType.MESSAGE);
        verify(typeDispatcher, never()).dispatch(any(), any(), any());
    }

    @Test
    void dispatch_shouldDispatchToAllSupportedUpdateTypeDispatchers() {
        Registry registry = mock(Registry.class);
        AuthorizationManager authorizationManager = mock(AuthorizationManager.class);

        UpdateTypeDispatcher firstDispatcher = mock(UpdateTypeDispatcher.class);
        UpdateTypeDispatcher secondDispatcher = mock(UpdateTypeDispatcher.class);
        UpdateTypeDispatcher unsupportedDispatcher = mock(UpdateTypeDispatcher.class);

        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);

        when(registry.getUpdateHandler(BOT_NAME)).thenReturn(null);
        when(update.type()).thenReturn(UpdateType.MESSAGE);

        when(firstDispatcher.supports(UpdateType.MESSAGE)).thenReturn(true);
        when(secondDispatcher.supports(UpdateType.MESSAGE)).thenReturn(true);
        when(unsupportedDispatcher.supports(UpdateType.MESSAGE)).thenReturn(false);

        Dispatcher dispatcher = new Dispatcher(
                BOT_NAME,
                registry,
                List.of(
                        firstDispatcher,
                        secondDispatcher,
                        unsupportedDispatcher
                ),
                authorizationManager
        );

        dispatcher.dispatch(update, context);

        verify(firstDispatcher).dispatch(BOT_NAME, update, context);
        verify(secondDispatcher).dispatch(BOT_NAME, update, context);
        verify(unsupportedDispatcher, never())
                .dispatch(any(), any(), any());
    }

    @Test
    void dispatch_shouldHandleUpdateAndDispatchUpdateType() {
        Registry registry = mock(Registry.class);
        Handler handler = mock(Handler.class);
        AuthorizationManager authorizationManager = mock(AuthorizationManager.class);
        UpdateTypeDispatcher typeDispatcher = mock(UpdateTypeDispatcher.class);

        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);

        when(registry.getUpdateHandler(BOT_NAME)).thenReturn(handler);
        when(authorizationManager.authorize(context, handler))
                .thenReturn(AuthorizationDecision.granted());

        when(update.type()).thenReturn(UpdateType.MESSAGE);
        when(typeDispatcher.supports(UpdateType.MESSAGE)).thenReturn(true);

        Dispatcher dispatcher = new Dispatcher(
                BOT_NAME,
                registry,
                List.of(typeDispatcher),
                authorizationManager
        );

        dispatcher.dispatch(update, context);

        verify(handler).handle(update, context);
        verify(typeDispatcher).dispatch(BOT_NAME, update, context);
    }

}