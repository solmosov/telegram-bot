package io.github.solmosov.telegrambot.scanner.resolver;

import io.github.solmosov.telegrambot.annotation.LocationHandler;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.model.MessageType;
import io.github.solmosov.telegrambot.registry.Registry;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LocationHandlerResolverTest {

    private final LocationHandlerResolver resolver =
            new LocationHandlerResolver();

    @Test
    void supports_shouldReturnTrue_whenMethodHasLocationHandlerAnnotation() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("location");

        assertTrue(resolver.supports(method));
    }

    @Test
    void supports_shouldReturnFalse_whenMethodDoesNotHaveLocationHandlerAnnotation() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("withoutAnnotation");

        assertFalse(resolver.supports(method));
    }

    @Test
    void register_shouldRegisterLocationHandlerWithValue() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("location");

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
                        registration.type() == MessageType.LOCATION
                                && registration.key().equals("myBotSend your location")
                                && registration.handler() == handler
                )
        );
    }

    @Test
    void register_shouldRegisterLocationHandlerWithNullKey_whenValueIsEmpty() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("locationWithoutValue");

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
                        registration.type() == MessageType.LOCATION
                                && registration.key().equals("myBot")
                                && registration.handler() == handler
                )
        );
    }

    static class TestHandler {

        @LocationHandler("Send your location")
        public void location() {
        }

        @LocationHandler
        public void locationWithoutValue() {
        }

        public void withoutAnnotation() {
        }
    }
}