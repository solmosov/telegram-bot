package io.github.solmosov.telegrambot.scanner.resolver;

import io.github.solmosov.telegrambot.annotation.CommandHandler;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.model.MessageType;
import io.github.solmosov.telegrambot.registry.Registry;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CommandHandlerResolverTest {

    private final CommandHandlerResolver resolver =
            new CommandHandlerResolver();

    @Test
    void supports_shouldReturnTrue_whenMethodHasCommandHandlerAnnotation() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("start");

        assertTrue(resolver.supports(method));
    }

    @Test
    void supports_shouldReturnFalse_whenMethodDoesNotHaveCommandHandlerAnnotation() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("withoutAnnotation");

        assertFalse(resolver.supports(method));
    }

    @Test
    void register_shouldRegisterCommandHandler() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("start");

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
                        registration.type() == MessageType.COMMAND
                                && registration.key().equals("myBot/start")
                                && registration.handler() == handler
                )
        );
    }


    static class TestHandler {

        @CommandHandler("/start")
        public void start() {
        }

        public void withoutAnnotation() {
        }
    }
}