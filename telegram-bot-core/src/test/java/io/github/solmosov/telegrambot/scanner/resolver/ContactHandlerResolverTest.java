package io.github.solmosov.telegrambot.scanner.resolver;

import io.github.solmosov.telegrambot.annotation.ContactHandler;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.model.MessageType;
import io.github.solmosov.telegrambot.registry.Registry;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ContactHandlerResolverTest {

    private final ContactHandlerResolver resolver =
            new ContactHandlerResolver();

    @Test
    void supports_shouldReturnTrue_whenMethodHasContactHandlerAnnotation() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("contact");

        assertTrue(resolver.supports(method));
    }

    @Test
    void supports_shouldReturnFalse_whenMethodDoesNotHaveContactHandlerAnnotation() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("withoutAnnotation");

        assertFalse(resolver.supports(method));
    }

    @Test
    void register_shouldRegisterContactHandler() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("contact");

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
                        registration.type() == MessageType.CONTACT
                                && registration.key().equals("myBot")
                                && registration.handler() == handler
                )
        );
    }

    static class TestHandler {

        @ContactHandler
        public void contact() {
        }

        public void withoutAnnotation() {
        }
    }
}