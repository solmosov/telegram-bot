package io.github.solmosov.telegrambot.registry.store;

import io.github.solmosov.telegrambot.exception.handler.HandlerRegistrationException;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.registry.registration.UpdateHandlerRegistration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class UpdateHandlerStoreTest {

    private static final String BOT_NAME = "myBot".toLowerCase();

    private UpdateHandlerStore store;

    @BeforeEach
    void setUp(){
        store = new UpdateHandlerStore(BOT_NAME);
    }

    @Test
    void shouldRegisterAndGetHandlerByBotName() {
        Handler handler = mock(Handler.class);

        store.register(new UpdateHandlerRegistration(
                BOT_NAME,
                handler
        ));

        Handler result = store.getHandler(BOT_NAME);

        assertSame(handler, result);
    }

    @Test
    void shouldReturnNullWhenHandlerIsNotRegistered() {
        Handler result = store.getHandler(BOT_NAME);

        assertNull(result);
    }

    @Test
    void shouldReturnNullWhenBotNameIsNotRegistered() {
        Handler handler = mock(Handler.class);

        store.register(new UpdateHandlerRegistration(
                BOT_NAME,
                handler
        ));

        Handler result = store.getHandler("another-bot");

        assertNull(result);
    }

    @Test
    void shouldThrowExceptionWhenHandlerAlreadyRegisteredForBot() {
        Handler firstHandler = mock(Handler.class);
        Handler secondHandler = mock(Handler.class);

        store.register(new UpdateHandlerRegistration(
                BOT_NAME,
                firstHandler
        ));

        HandlerRegistrationException exception = assertThrows(
                HandlerRegistrationException.class,
                () -> store.register(new UpdateHandlerRegistration(
                        BOT_NAME,
                        secondHandler
                ))
        );

        assertEquals(
                "UpdateHandler already registered for bot='mybot'",
                exception.getMessage()
        );
    }

    @Test
    void shouldKeepFirstHandlerWhenDuplicateRegistrationOccurs() {
        Handler firstHandler = mock(Handler.class);
        Handler secondHandler = mock(Handler.class);

        store.register(new UpdateHandlerRegistration(
                BOT_NAME,
                firstHandler
        ));

        assertThrows(
                HandlerRegistrationException.class,
                () -> store.register(new UpdateHandlerRegistration(
                        BOT_NAME,
                        secondHandler
                ))
        );

        assertSame(
                firstHandler,
                store.getHandler(BOT_NAME)
        );
    }

    @Test
    void shouldRegisterHandlersForDifferentBots() {
        Handler firstHandler = mock(Handler.class);
        Handler secondHandler = mock(Handler.class);

        store.register(new UpdateHandlerRegistration(
                "bot-one",
                firstHandler
        ));

        store.register(new UpdateHandlerRegistration(
                "bot-two",
                secondHandler
        ));

        assertSame(firstHandler, store.getHandler("bot-one"));
        assertSame(secondHandler, store.getHandler("bot-two"));
    }
}