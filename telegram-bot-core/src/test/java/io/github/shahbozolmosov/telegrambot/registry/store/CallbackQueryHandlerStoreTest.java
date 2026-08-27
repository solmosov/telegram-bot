package io.github.shahbozolmosov.telegrambot.registry.store;

import io.github.shahbozolmosov.telegrambot.exception.handler.HandlerRegistrationException;
import io.github.shahbozolmosov.telegrambot.handler.Handler;
import io.github.shahbozolmosov.telegrambot.registry.registration.CallbackQueryHandlerRegistration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CallbackQueryHandlerStoreTest {

    private static final String BOT_NAME = "myBot";

    private CallbackQueryHandlerStore store;

    @BeforeEach
    void setUp() {
        store = new CallbackQueryHandlerStore(BOT_NAME);
    }

    @Test
    void shouldRegisterAndFindHandlerByKey() {
        Handler handler = mock(Handler.class);

        store.register(new CallbackQueryHandlerRegistration(
                BOT_NAME + "hello",
                handler
        ));

        List<Handler> result = store.find(BOT_NAME + "hello");

        assertEquals(List.of(handler), result);
    }

    @Test
    void shouldReturnEmptyListWhenHandlerIsNotRegistered() {
        List<Handler> result = store.find(BOT_NAME + "hello");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenHandlerAlreadyRegisteredForSameKey() {
        Handler firstHandler = mock(Handler.class);
        Handler secondHandler = mock(Handler.class);

        store.register(new CallbackQueryHandlerRegistration(
                BOT_NAME + "hello",
                firstHandler
        ));

        HandlerRegistrationException exception = assertThrows(
                HandlerRegistrationException.class,
                () -> store.register(new CallbackQueryHandlerRegistration(
                        BOT_NAME + "hello",
                        secondHandler
                ))
        );

        assertEquals(
                "CallbackQueryHandler already registered for key='hello' in bot='myBot'",
                exception.getMessage()
        );
    }

    @Test
    void shouldKeepFirstHandlerWhenDuplicateRegistrationOccurs() {
        Handler firstHandler = mock(Handler.class);
        Handler secondHandler = mock(Handler.class);

        store.register(new CallbackQueryHandlerRegistration(
                BOT_NAME + "hello",
                firstHandler
        ));

        assertThrows(
                HandlerRegistrationException.class,
                () -> store.register(new CallbackQueryHandlerRegistration(
                        BOT_NAME + "hello",
                        secondHandler
                ))
        );

        assertEquals(
                List.of(firstHandler),
                store.find(BOT_NAME + "hello")
        );
    }

    @Test
    void shouldReturnGlobalHandler() {
        Handler globalHandler = mock(Handler.class);

        store.register(new CallbackQueryHandlerRegistration(
                BOT_NAME,
                globalHandler
        ));

        List<Handler> result = store.find(BOT_NAME + "hello");

        assertEquals(
                List.of(globalHandler),
                result
        );
    }

    @Test
    void shouldReturnExactHandlerAndGlobalHandler() {
        Handler exactHandler = mock(Handler.class);
        Handler globalHandler = mock(Handler.class);

        store.register(new CallbackQueryHandlerRegistration(
                BOT_NAME + "hello",
                exactHandler
        ));

        store.register(new CallbackQueryHandlerRegistration(
                BOT_NAME,
                globalHandler
        ));

        List<Handler> result = store.find(BOT_NAME + "hello");

        assertEquals(
                List.of(exactHandler, globalHandler),
                result
        );
    }

    @Test
    void shouldReturnHandlersForDifferentKeys() {
        Handler firstHandler = mock(Handler.class);
        Handler secondHandler = mock(Handler.class);

        store.register(new CallbackQueryHandlerRegistration(
                BOT_NAME + "hello",
                firstHandler
        ));

        store.register(new CallbackQueryHandlerRegistration(
                BOT_NAME + "bye",
                secondHandler
        ));

        assertEquals(
                List.of(firstHandler),
                store.find(BOT_NAME + "hello")
        );

        assertEquals(
                List.of(secondHandler),
                store.find(BOT_NAME + "bye")
        );
    }

}