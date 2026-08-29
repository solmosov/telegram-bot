package io.github.solmosov.telegrambot.scanner.resolver;

import io.github.solmosov.telegrambot.annotation.PhotoHandler;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.model.MessageType;
import io.github.solmosov.telegrambot.registry.Registry;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PhotoHandlerResolverTest {

    private final PhotoHandlerResolver resolver =
            new PhotoHandlerResolver();

    @Test
    void supports_shouldReturnTrue_whenMethodHasPhotoHandlerAnnotation() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("photo");

        assertTrue(resolver.supports(method));
    }

    @Test
    void supports_shouldReturnFalse_whenMethodDoesNotHavePhotoHandlerAnnotation() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("withoutAnnotation");

        assertFalse(resolver.supports(method));
    }

    @Test
    void register_shouldRegisterPhotoHandler() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("photo");

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
                        registration.type() == MessageType.PHOTO
                                && registration.key().equals("myBot")
                                && registration.handler() == handler
                )
        );
    }

    static class TestHandler {

        @PhotoHandler
        public void photo() {
        }

        public void withoutAnnotation() {
        }
    }
}