package io.github.shahbozolmosov.telegrambot.scanner;

import io.github.shahbozolmosov.telegrambot.annotation.BotHandler;
import io.github.shahbozolmosov.telegrambot.exception.TelegramBotException;
import io.github.shahbozolmosov.telegrambot.handler.Handler;
import io.github.shahbozolmosov.telegrambot.handler.argument.HandlerArgumentResolverComposite;
import io.github.shahbozolmosov.telegrambot.registry.Registry;
import io.github.shahbozolmosov.telegrambot.scanner.resolver.HandlerAnnotationResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class HandlerRegistrarTest {

    private ClassScanner scanner;
    private ClassInstanceFactory factory;
    private Registry registry;
    private HandlerAnnotationResolver resolver;
    private HandlerArgumentResolverComposite argumentResolver;

    private HandlerRegistrar registrar;

    @BeforeEach
    void setUp() {
        scanner = mock(ClassScanner.class);
        factory = mock(ClassInstanceFactory.class);
        registry = mock(Registry.class);
        resolver = mock(HandlerAnnotationResolver.class);
        argumentResolver = mock(HandlerArgumentResolverComposite.class);

        registrar = new HandlerRegistrar(
                scanner,
                factory,
                registry,
                List.of(resolver),
                argumentResolver,
                "myBot"
        );
    }

    @Test
    void shouldThrowExceptionWhenHandlerClassIsNotAnnotated() {
        Object instance = new Object();

        TelegramBotException exception = assertThrows(
                TelegramBotException.class,
                () -> registrar.register(instance)
        );

        assertEquals(
                "Handler class must be annotated with @BotHandler",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenBotNameIsBlank() {
        Object instance = new InvalidBotHandler();

        TelegramBotException exception = assertThrows(
                TelegramBotException.class,
                () -> registrar.register(instance)
        );

        assertEquals(
                "@BotHandler bot name is required",
                exception.getMessage()
        );
    }

    @Test
    void shouldIgnoreHandlerWhenBotNameDoesNotMatch() {
        Object instance = new AnotherBotHandler();

        registrar.register(instance);

        verifyNoInteractions(resolver);
        verifyNoInteractions(registry);
    }

    @Test
    void shouldRegisterSupportedHandlerMethod() throws NoSuchMethodException {
        Object instance = new TestBot();

        Method method = TestBot.class.getDeclaredMethod("supported");

        when(resolver.supports(method)).thenReturn(true);

        registrar.register(instance);

        verify(resolver).register(
                eq("myBot"),
                eq(method),
                any(Handler.class),
                eq(registry)
        );
    }

    @Test
    void shouldNotRegisterUnsupportedHandlerMethod() {
        Object instance = new TestBot();

        when(resolver.supports(any(Method.class))).thenReturn(false);

        registrar.register(instance);

        verify(resolver, never()).register(
                anyString(),
                any(Method.class),
                any(Handler.class),
                eq(registry)
        );
    }

    @Test
    void shouldRegisterOnlySupportedMethods() throws NoSuchMethodException {
        Object instance = new TestBot();

        Method supported = TestBot.class.getDeclaredMethod("supported");
        Method unsupported = TestBot.class.getDeclaredMethod("unsupported");

        when(resolver.supports(supported)).thenReturn(true);
        when(resolver.supports(unsupported)).thenReturn(false);

        registrar.register(instance);

        verify(resolver).register(
                eq("myBot"),
                eq(supported),
                any(Handler.class),
                eq(registry)
        );

        verify(resolver, never()).register(
                eq("myBot"),
                eq(unsupported),
                any(Handler.class),
                eq(registry)
        );
    }


    @Test
    void shouldScanAndCreateInstancesWhenRegisteringPackage() {
        Class<?> handlerClass = TestBot.class;
        TestBot instance = new TestBot();

        when(scanner.scan("com.example.handlers", BotHandler.class))
                .thenReturn(List.of(handlerClass));

        when(factory.create(handlerClass))
                .thenReturn(instance);

        registrar.register("com.example.handlers");

        verify(scanner).scan(
                "com.example.handlers",
                BotHandler.class
        );

        verify(factory).create(handlerClass);
    }

    @Test
    void shouldRegisterCreatedHandlerWhenRegisteringPackage() {
        Class<?> handlerClass = TestBot.class;
        TestBot instance = new TestBot();

        when(scanner.scan(anyString(), eq(BotHandler.class)))
                .thenReturn(List.of(handlerClass));

        when(factory.create(handlerClass))
                .thenReturn(instance);

        when(resolver.supports(any(Method.class)))
                .thenReturn(true);

        registrar.register("com.example.handlers");

        verify(factory).create(handlerClass);

        verify(resolver, atLeastOnce()).register(
                eq("myBot"),
                any(Method.class),
                any(Handler.class),
                eq(registry)
        );
    }

    @Test
    void shouldRegisterHandlerInstance() throws NoSuchMethodException {
        TestBot instance = new TestBot();

        Method method = TestBot.class.getDeclaredMethod("supported");

        when(resolver.supports(method)).thenReturn(true);

        registrar.register(instance);

        verify(resolver).register(
                eq("myBot"),
                eq(method),
                any(Handler.class),
                eq(registry)
        );
    }

    @BotHandler("")
    static class InvalidBotHandler {
    }

    @BotHandler("another-bot")
    static class AnotherBotHandler {

        public void handler() {
        }
    }

    @BotHandler("myBot")
    private class TestBot {
        public void supported() {
        }

        public void unsupported() {
        }
    }
}