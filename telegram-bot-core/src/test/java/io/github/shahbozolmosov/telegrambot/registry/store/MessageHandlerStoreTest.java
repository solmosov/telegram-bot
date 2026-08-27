package io.github.shahbozolmosov.telegrambot.registry.store;

import io.github.shahbozolmosov.telegrambot.exception.handler.HandlerRegistrationException;
import io.github.shahbozolmosov.telegrambot.handler.Handler;
import io.github.shahbozolmosov.telegrambot.model.MessageType;
import io.github.shahbozolmosov.telegrambot.registry.registration.MessageHandlerRegistration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MessageHandlerStoreTest {

    private static final String BOT_NAME = "myBot";

    private MessageHandlerStore store;

    @BeforeEach
    void setUp() {
        store = new MessageHandlerStore(BOT_NAME);
    }

    @Test
    void shouldRegisterAndFindHandlerByTypeAndKey() {
        Handler handler = mock(Handler.class);

        store.register(new MessageHandlerRegistration(
                MessageType.TEXT,
                "hello",
                handler
        ));

        List<Handler> result = store.find(
                MessageType.TEXT,
                "hello"
        );

        assertEquals(List.of(handler), result);
    }

    @Test
    void shouldReturnEmptyListWhenTypeIsNotRegistered() {
        List<Handler> result = store.find(
                MessageType.TEXT,
                "hello"
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenKeyIsNotRegistered() {
        Handler handler = mock(Handler.class);

        store.register(new MessageHandlerRegistration(
                MessageType.TEXT,
                "hello",
                handler
        ));

        List<Handler> result = store.find(
                MessageType.TEXT,
                "bye"
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldNotFindHandlerRegisteredForAnotherType() {
        Handler handler = mock(Handler.class);

        store.register(new MessageHandlerRegistration(
                MessageType.TEXT,
                "hello",
                handler
        ));

        List<Handler> result = store.find(
                MessageType.COMMAND,
                "hello"
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnGlobalHandler() {
        Handler globalHandler = mock(Handler.class);

        store.register(new MessageHandlerRegistration(
                MessageType.TEXT,
                BOT_NAME,
                globalHandler
        ));

        List<Handler> result = store.find(
                MessageType.TEXT,
                "hello"
        );

        assertEquals(List.of(globalHandler), result);
    }

    @Test
    void shouldReturnExactHandlerAndGlobalHandler() {
        Handler exactHandler = mock(Handler.class);
        Handler globalHandler = mock(Handler.class);

        store.register(new MessageHandlerRegistration(
                MessageType.TEXT,
                "hello",
                exactHandler
        ));

        store.register(new MessageHandlerRegistration(
                MessageType.TEXT,
                BOT_NAME,
                globalHandler
        ));

        List<Handler> result = store.find(
                MessageType.TEXT,
                "hello"
        );

        assertEquals(
                List.of(exactHandler, globalHandler),
                result
        );
    }

    @Test
    void shouldReturnOnlyExactHandlerWhenGlobalHandlerDoesNotExist() {
        Handler exactHandler = mock(Handler.class);

        store.register(new MessageHandlerRegistration(
                MessageType.TEXT,
                "hello",
                exactHandler
        ));

        List<Handler> result = store.find(
                MessageType.TEXT,
                "hello"
        );

        assertEquals(List.of(exactHandler), result);
    }

    @Test
    void shouldThrowExceptionWhenHandlerAlreadyRegisteredForSameTypeAndKey() {
        Handler firstHandler = mock(Handler.class);
        Handler secondHandler = mock(Handler.class);

        store.register(new MessageHandlerRegistration(
                MessageType.TEXT,
                "hello",
                firstHandler
        ));

        HandlerRegistrationException exception = assertThrows(
                HandlerRegistrationException.class,
                () -> store.register(new MessageHandlerRegistration(
                        MessageType.TEXT,
                        "hello",
                        secondHandler
                ))
        );

        assertEquals(
                "MessageHandler already registered for type='TEXT' and key='hello' in bot='myBot'",
                exception.getMessage()
        );
    }

    @Test
    void shouldAllowSameKeyForDifferentTypes() {
        Handler textHandler = mock(Handler.class);
        Handler commandHandler = mock(Handler.class);

        store.register(new MessageHandlerRegistration(
                MessageType.TEXT,
                "hello",
                textHandler
        ));

        store.register(new MessageHandlerRegistration(
                MessageType.COMMAND,
                "hello",
                commandHandler
        ));

        assertEquals(
                List.of(textHandler),
                store.find(MessageType.TEXT, "hello")
        );

        assertEquals(
                List.of(commandHandler),
                store.find(MessageType.COMMAND, "hello")
        );
    }

    @Test
    void shouldThrowExceptionWhenGlobalHandlerAlreadyRegistered() {
        Handler firstHandler = mock(Handler.class);
        Handler secondHandler = mock(Handler.class);

        store.register(new MessageHandlerRegistration(
                MessageType.TEXT,
                BOT_NAME,
                firstHandler
        ));

        assertThrows(
                HandlerRegistrationException.class,
                () -> store.register(new MessageHandlerRegistration(
                        MessageType.TEXT,
                        BOT_NAME,
                        secondHandler
                ))
        );
    }
}