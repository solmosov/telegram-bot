
package io.github.solmosov.telegrambot.dispatcher;

import io.github.solmosov.telegrambot.authorization.AuthorizationDecision;
import io.github.solmosov.telegrambot.authorization.AuthorizationManager;
import io.github.solmosov.telegrambot.context.BotContext;
import io.github.solmosov.telegrambot.context.CallbackQueryContext;
import io.github.solmosov.telegrambot.exception.authorization.AccessDeniedException;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.model.CallbackQuery;
import io.github.solmosov.telegrambot.model.Update;
import io.github.solmosov.telegrambot.model.UpdateType;
import io.github.solmosov.telegrambot.registry.Registry;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CallbackQueryUpdateDispatcherTest {

    private static final String BOT_NAME = "myBot";

    @Test
    void supports_shouldReturnTrue_whenTypeIsCallbackQuery() {
        Registry registry = mock(Registry.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        CallbackQueryUpdateDispatcher dispatcher =
                new CallbackQueryUpdateDispatcher(
                        registry,
                        authorizationManager
                );

        assertTrue(dispatcher.supports(UpdateType.CALLBACK_QUERY));
    }

    @Test
    void supports_shouldReturnFalse_whenTypeIsNotCallbackQuery() {
        Registry registry = mock(Registry.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        CallbackQueryUpdateDispatcher dispatcher =
                new CallbackQueryUpdateDispatcher(
                        registry,
                        authorizationManager
                );

        assertFalse(dispatcher.supports(UpdateType.MESSAGE));
    }

    @Test
    void dispatch_shouldHandleCallbackQuery_whenHandlerIsAuthorized() {
        Registry registry = mock(Registry.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        Handler handler = mock(Handler.class);
        BotContext botContext = mock(BotContext.class);
        CallbackQueryContext callbackQueryContext =
                mock(CallbackQueryContext.class);

        Update update = mock(Update.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);

        when(update.callbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.data()).thenReturn("buy");

        when(registry.findCallbackQuery("myBotbuy"))
                .thenReturn(List.of(handler));

        when(authorizationManager.authorize(botContext, handler))
                .thenReturn(AuthorizationDecision.granted());

        when(handler.getCallbackPattern())
                .thenReturn("buy");

        when(botContext.callbackQuery())
                .thenReturn(callbackQueryContext);

        CallbackQueryUpdateDispatcher dispatcher =
                new CallbackQueryUpdateDispatcher(
                        registry,
                        authorizationManager
                );

        dispatcher.dispatch(BOT_NAME, update, botContext);

        verify(registry).findCallbackQuery("myBotbuy");
        verify(authorizationManager).authorize(botContext, handler);
        verify(callbackQueryContext).setCallbackParams(Map.of());
        verify(handler).handle(update, botContext);
    }

    @Test
    void dispatch_shouldUseBotNameAndCallbackDataAsKey() {
        Registry registry = mock(Registry.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        BotContext botContext = mock(BotContext.class);
        Update update = mock(Update.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);

        when(update.callbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.data()).thenReturn("product:123");

        when(registry.findCallbackQuery("myBotproduct:123"))
                .thenReturn(List.of());

        CallbackQueryUpdateDispatcher dispatcher =
                new CallbackQueryUpdateDispatcher(
                        registry,
                        authorizationManager
                );

        dispatcher.dispatch(BOT_NAME, update, botContext);

        verify(registry).findCallbackQuery("myBotproduct:123");
    }

    @Test
    void dispatch_shouldSetCallbackParams_whenHandlerHasCallbackPattern() {
        Registry registry = mock(Registry.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        Handler handler = mock(Handler.class);
        BotContext botContext = mock(BotContext.class);
        CallbackQueryContext callbackQueryContext =
                mock(CallbackQueryContext.class);

        Update update = mock(Update.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);

        when(update.callbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.data()).thenReturn("product:123");

        when(registry.findCallbackQuery("myBotproduct:123"))
                .thenReturn(List.of(handler));

        when(authorizationManager.authorize(botContext, handler))
                .thenReturn(AuthorizationDecision.granted());

        when(handler.getCallbackPattern())
                .thenReturn("product:{id|long}");

        when(botContext.callbackQuery())
                .thenReturn(callbackQueryContext);

        CallbackQueryUpdateDispatcher dispatcher =
                new CallbackQueryUpdateDispatcher(
                        registry,
                        authorizationManager
                );

        dispatcher.dispatch(BOT_NAME, update, botContext);

        verify(callbackQueryContext).setCallbackParams(
                Map.of("id", 123L)
        );

        verify(handler).handle(update, botContext);
    }

    @Test
    void dispatch_shouldThrowAccessDeniedException_whenAuthorizationDenied() {
        Registry registry = mock(Registry.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        Handler handler = mock(Handler.class);
        BotContext botContext = mock(BotContext.class);

        Update update = mock(Update.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);

        when(update.callbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.data()).thenReturn("buy");

        when(registry.findCallbackQuery("myBotbuy"))
                .thenReturn(List.of(handler));

        when(authorizationManager.authorize(botContext, handler))
                .thenReturn(AuthorizationDecision.denied());

        CallbackQueryUpdateDispatcher dispatcher =
                new CallbackQueryUpdateDispatcher(
                        registry,
                        authorizationManager
                );

        assertThrows(
                AccessDeniedException.class,
                () -> dispatcher.dispatch(BOT_NAME, update, botContext)
        );

        verify(handler, never()).handle(any(), any());
        verify(botContext, never()).callbackQuery();
    }

    @Test
    void dispatch_shouldHandleAllAuthorizedHandlers() {
        Registry registry = mock(Registry.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        Handler firstHandler = mock(Handler.class);
        Handler secondHandler = mock(Handler.class);

        BotContext botContext = mock(BotContext.class);
        CallbackQueryContext callbackQueryContext =
                mock(CallbackQueryContext.class);

        Update update = mock(Update.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);

        when(update.callbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.data()).thenReturn("buy");

        when(registry.findCallbackQuery("myBotbuy"))
                .thenReturn(List.of(firstHandler, secondHandler));

        when(authorizationManager.authorize(botContext, firstHandler))
                .thenReturn(AuthorizationDecision.granted());

        when(authorizationManager.authorize(botContext, secondHandler))
                .thenReturn(AuthorizationDecision.granted());

        when(firstHandler.getCallbackPattern())
                .thenReturn("buy");

        when(secondHandler.getCallbackPattern())
                .thenReturn("buy");

        when(botContext.callbackQuery())
                .thenReturn(callbackQueryContext);

        CallbackQueryUpdateDispatcher dispatcher =
                new CallbackQueryUpdateDispatcher(
                        registry,
                        authorizationManager
                );

        dispatcher.dispatch(BOT_NAME, update, botContext);

        verify(firstHandler).handle(update, botContext);
        verify(secondHandler).handle(update, botContext);

        verify(callbackQueryContext, times(2))
                .setCallbackParams(Map.of());
    }
}
