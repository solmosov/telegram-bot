package io.github.solmosov.telegrambot.scanner.resolver;

import io.github.solmosov.telegrambot.annotation.UsersSharedHandler;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.model.MessageType;
import io.github.solmosov.telegrambot.registry.Registry;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UsersSharedHandlerResolverTest {

    private final UsersSharedHandlerResolver resolver =
            new UsersSharedHandlerResolver();

    @Test
    void supports_shouldReturnTrue_whenMethodHasUsersSharedHandlerAnnotation() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("usersShared");

        assertTrue(resolver.supports(method));
    }

    @Test
    void supports_shouldReturnFalse_whenMethodDoesNotHaveUsersSharedHandlerAnnotation() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("withoutAnnotation");

        assertFalse(resolver.supports(method));
    }

    @Test
    void register_shouldRegisterUsersSharedHandlerWithRequestId() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("usersShared");

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
                        registration.type() == MessageType.USERS_SHARED
                                && registration.key().equals("myBot123")
                                && registration.handler() == handler
                )
        );
    }

    @Test
    void register_shouldRegisterUsersSharedHandlerWithoutRequestId() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("usersSharedWithoutRequestId");

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
                        registration.type() == MessageType.USERS_SHARED
                                && registration.key().equals("myBot")
                                && registration.handler() == handler
                )
        );
    }

    static class TestHandler {

        @UsersSharedHandler(123)
        public void usersShared() {
        }

        @UsersSharedHandler
        public void usersSharedWithoutRequestId() {
        }

        public void withoutAnnotation() {
        }
    }
}