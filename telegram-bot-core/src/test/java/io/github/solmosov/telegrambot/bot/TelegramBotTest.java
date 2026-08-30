package io.github.solmosov.telegrambot.bot;

import io.github.solmosov.telegrambot.exception.TelegramBotException;
import io.github.solmosov.telegrambot.scanner.ApplicationPackageResolver;
import io.github.solmosov.telegrambot.scanner.HandlerRegistrar;
import io.github.solmosov.telegrambot.source.UpdateSource;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TelegramBotTest {

    @Test
    void shouldCreateBotWithName() {
        TelegramBot bot = new TelegramBot(
                "test-bot",
                "fake-token"
        );

        assertEquals("test-bot", bot.getName());
    }

    @Test
    void shouldCreateBotWithCustomConfig() {
        TelegramBotConfig config = TelegramBotConfig.builder()
                .handlerRegistrationMode(HandlerRegistrationMode.EXTERNAL)
                .updateMode(UpdatesMode.POLLING)
                .build();

        TelegramBot bot = new TelegramBot(
                "test-bot",
                "fake-token",
                config
        );

        assertEquals("test-bot", bot.getName());
    }

    @Test
    void shouldStartBot() {
        UpdateSource updateSource = mock(UpdateSource.class);

        TelegramBotConfig config = TelegramBotConfig.builder()
                .handlerRegistrationMode(HandlerRegistrationMode.EXTERNAL)
                .updateMode(UpdatesMode.POLLING)
                .build();

        TelegramBot bot = new TelegramBot(
                "test-bot",
                "fake-token",
                config,
                updateSource
        );

        bot.start();

        verify(updateSource).start();

        bot.stopBot();
    }

    @Test
    void shouldThrowExceptionWhenBotAlreadyStarted() {
        UpdateSource updateSource = mock(UpdateSource.class);

        TelegramBotConfig config = TelegramBotConfig.builder()
                .handlerRegistrationMode(HandlerRegistrationMode.EXTERNAL)
                .updateMode(UpdatesMode.POLLING)
                .build();

        TelegramBot bot = new TelegramBot(
                "test-bot",
                "fake-token",
                config,
                updateSource
        );

        bot.start();

        assertThrows(
                TelegramBotException.class,
                bot::start
        );

        bot.stopBot();
    }

    @Test
    void shouldStopBotAfterStart() {
        UpdateSource updateSource = mock(UpdateSource.class);

        TelegramBotConfig config = TelegramBotConfig.builder()
                .handlerRegistrationMode(HandlerRegistrationMode.EXTERNAL)
                .updateMode(UpdatesMode.POLLING)
                .build();

        TelegramBot bot = new TelegramBot(
                "test-bot",
                "fake-token",
                config,
                updateSource
        );

        bot.start();
        bot.stopBot();

        verify(updateSource).stop();
        verify(updateSource).shutdown();
    }

    @Test
    void shouldRegisterHandler() {
        UpdateSource updateSource = mock(UpdateSource.class);
        HandlerRegistrar handlerRegistrar = mock(HandlerRegistrar.class);

        TelegramBotConfig config = TelegramBotConfig.builder()
                .handlerRegistrationMode(HandlerRegistrationMode.EXTERNAL)
                .updateMode(UpdatesMode.POLLING)
                .build();

        TelegramBot bot = new TelegramBot(
                "test-bot",
                "fake-token",
                config,
                updateSource,
                handlerRegistrar
        );

        Object handler = new Object();

        bot.registerHandler(handler);

        verify(handlerRegistrar).register(handler);
    }

    @Test
    void shouldScanHandlersWhenRegistrationModeIsClasspathScan() {
        UpdateSource updateSource = mock(UpdateSource.class);
        HandlerRegistrar handlerRegistrar = mock(HandlerRegistrar.class);
        ApplicationPackageResolver packageResolver =
                mock(ApplicationPackageResolver.class);

        when(packageResolver.resolve())
                .thenReturn("com.example.bot");

        TelegramBotConfig config = TelegramBotConfig.builder()
                .handlerRegistrationMode(HandlerRegistrationMode.CLASSPATH_SCAN)
                .updateMode(UpdatesMode.POLLING)
                .build();

        TelegramBot bot = new TelegramBot(
                "test-bot",
                "fake-token",
                config,
                updateSource,
                handlerRegistrar,
                packageResolver
        );

        bot.start();

        verify(packageResolver).resolve();
        verify(handlerRegistrar).register("com.example.bot");
        verify(updateSource).start();

        bot.stopBot();
    }

    @Test
    void shouldNotScanHandlersWhenRegistrationModeIsExternal() {
        UpdateSource updateSource = mock(UpdateSource.class);
        HandlerRegistrar handlerRegistrar = mock(HandlerRegistrar.class);
        ApplicationPackageResolver applicationPackageResolver =
                mock(ApplicationPackageResolver.class);

        TelegramBotConfig config = TelegramBotConfig.builder()
                .handlerRegistrationMode(HandlerRegistrationMode.EXTERNAL)
                .updateMode(UpdatesMode.POLLING)
                .build();

        TelegramBot bot = new TelegramBot(
                "test-bot",
                "fake-token",
                config,
                updateSource,
                handlerRegistrar,
                applicationPackageResolver
        );

        bot.start();

        verify(applicationPackageResolver, never()).resolve();
        verify(handlerRegistrar, never()).register(anyString());
        verify(updateSource).start();

        bot.stopBot();
    }

    @Test
    void shouldStopUpdateSource() {
        UpdateSource updateSource = mock(UpdateSource.class);

        TelegramBot bot = new TelegramBot(
                "test-bot",
                "fake-token",
                TelegramBotConfig.defaults(),
                updateSource
        );

        bot.stopBot();

        verify(updateSource).stop();
        verify(updateSource).shutdown();
    }

    @Test
    void shouldStopBeforeShutdown() {
        UpdateSource updateSource = mock(UpdateSource.class);

        TelegramBot bot = new TelegramBot(
                "test-bot",
                "fake-token",
                TelegramBotConfig.defaults(),
                updateSource
        );

        bot.stopBot();

        InOrder inOrder = inOrder(updateSource);

        inOrder.verify(updateSource).stop();
        inOrder.verify(updateSource).shutdown();
    }

    @Test
    void shouldAllowRestartAfterStop() {
        UpdateSource updateSource = mock(UpdateSource.class);

        TelegramBot bot = new TelegramBot(
                "test-bot",
                "fake-token",
                TelegramBotConfig.defaults(),
                updateSource
        );

        bot.start();
        bot.stopBot();

        bot.start();

        verify(updateSource, times(2)).start();

        bot.stopBot();
    }

    @Test
    void shouldPropagateExceptionWhenUpdateSourceFailsToStart() {
        UpdateSource updateSource = mock(UpdateSource.class);

        RuntimeException exception = new RuntimeException("Start failed");

        doThrow(exception)
                .when(updateSource)
                .start();

        TelegramBot bot = new TelegramBot(
                "test-bot",
                "fake-token",
                TelegramBotConfig.defaults(),
                updateSource
        );

        assertThrows(
                RuntimeException.class,
                bot::start
        );

        verify(updateSource).start();
    }

    @Test
    void shouldAllowStartAfterPreviousStartFailure() {
        UpdateSource updateSource = mock(UpdateSource.class);

        doThrow(new RuntimeException("Start failed"))
                .doNothing()
                .when(updateSource)
                .start();

        TelegramBot bot = new TelegramBot(
                "test-bot",
                "fake-token",
                TelegramBotConfig.defaults(),
                updateSource
        );

        assertThrows(
                RuntimeException.class,
                bot::start
        );

        bot.start();

        verify(updateSource, times(2)).start();

        bot.stopBot();
    }

    @Test
    void shouldProvideTelegramMessaging() {
        TelegramBot bot = new TelegramBot(
                "test-bot",
                "fake-token"
        );

        assertNotNull(bot.messaging());
    }
}