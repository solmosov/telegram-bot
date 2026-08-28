package io.github.solmosov.telegrambot.scanner.resolver;

import io.github.solmosov.telegrambot.annotation.CallbackQueryHandler;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.registry.Registry;
import io.github.solmosov.telegrambot.registry.registration.CallbackQueryHandlerRegistration;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CallbackQueryHandlerResolverTest {

    private final CallbackQueryHandlerResolver resolver = new CallbackQueryHandlerResolver();

    @Test
    void supports_shouldReturnTrue_whenMethodHasCallbackQueryHandlerAnnotation() throws NoSuchMethodException {
        Method method = TestHandler.class.getDeclaredMethod("callback");

        assertTrue(resolver.supports(method));
    }

    @Test
    void supports_shouldReturnFalse_whenMethodDoesNotHaveCallbackQueryHandlerAnnotation() throws NoSuchMethodException {
        Method method = TestHandler.class.getDeclaredMethod("withoutAnnotation");

        assertFalse(resolver.supports(method));
    }

    @Test
    void register_shouldRegisterCallbackHandler() throws NoSuchMethodException {
        Method method = TestHandler.class.getDeclaredMethod("callback");

        Handler handler = mock(Handler.class);
        Registry registry = mock(Registry.class);

        resolver.register(
                "myBot",
                method,
                handler,
                registry
        );

        verify(handler).setCallbackPattern("buy");

        verify(registry).registerCallbackQuery(any(CallbackQueryHandlerRegistration.class));
    }

    @Test
    void register_shouldUseBotNameAsKey_whenCallbackValueIsEmpty() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("callbackWithoutValue");

        Handler handler = mock(Handler.class);
        Registry registry = mock(Registry.class);

        resolver.register(
                "myBot",
                method,
                handler,
                registry
        );
        
        verify(registry).registerCallbackQuery(
                argThat(registration ->
                        registration.key().equals("myBot")
                                && registration.handler().equals(handler)
                )
        );
    }

    @Test
    void register_shouldCreateKeyFromBotNameAndCallbackValue() throws Exception {
        Method method = TestHandler.class.getDeclaredMethod("callback");

        Handler handler = mock(Handler.class);
        Registry registry = mock(Registry.class);

        resolver.register(
                "myBot",
                method,
                handler,
                registry
        );

        verify(registry).registerCallbackQuery(
                argThat(registration ->
                        registration.key().equals("myBotbuy")
                )
        );
    }

    static class TestHandler {

        @CallbackQueryHandler("buy")
        public void callback() {
        }

        @CallbackQueryHandler
        public void callbackWithoutValue() {

        }

        public void withoutAnnotation() {
        }
    }

}