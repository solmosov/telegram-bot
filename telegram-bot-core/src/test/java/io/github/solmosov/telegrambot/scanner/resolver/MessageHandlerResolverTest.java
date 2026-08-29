package io.github.solmosov.telegrambot.scanner.resolver;

import io.github.solmosov.telegrambot.annotation.MessageHandler;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.model.MessageType;
import io.github.solmosov.telegrambot.registry.Registry;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class MessageHandlerResolverTest {

    private final MessageHandlerResolver resolver =
            new MessageHandlerResolver();

    @Test
    void supports_shouldReturnTrue_whenMethodHasMessageHandlerAnnotation() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("message");

        assertTrue(resolver.supports(method));
    }

    @Test
    void supports_shouldReturnFalse_whenMethodDoesNotHaveMessageHandlerAnnotation() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("withoutAnnotation");

        assertFalse(resolver.supports(method));
    }

    @Test
    void register_shouldRegisterTextHandlerWithValue() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("message");

        Handler handler = mock(Handler.class);
        Registry registry = mock(Registry.class);

        resolver.register(
                "myBot",
                method,
                handler,
                registry
        );

        verify(registry).register(
                argThat(registration ->
                        registration.type() == MessageType.TEXT
                                && registration.key().equals("myBotHello")
                                && registration.handler() == handler
                )
        );
    }

    @Test
    void register_shouldRegisterTextHandlerWithNullKey_whenValueIsEmpty() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("messageWithoutValue");

        Handler handler = mock(Handler.class);
        Registry registry = mock(Registry.class);

        resolver.register(
                "myBot",
                method,
                handler,
                registry
        );

        verify(registry).register(
                argThat(registration ->
                        registration.type() == MessageType.TEXT
                                && registration.key().equals("myBot")
                                && registration.handler() == handler
                )
        );
    }

    static class TestHandler {

        @MessageHandler("Hello")
        public void message() {
        }

        @MessageHandler
        public void messageWithoutValue() {
        }

        public void withoutAnnotation() {
        }
    }
}