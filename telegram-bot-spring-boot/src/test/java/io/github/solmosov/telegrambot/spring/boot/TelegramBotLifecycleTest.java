package io.github.solmosov.telegrambot.spring.boot;

import io.github.solmosov.telegrambot.bot.TelegramBotApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TelegramBotLifecycleTest {

    private TelegramBotApplication application;
    private TelegramBotLifecycle lifecycle;

    @BeforeEach
    void setUp() {
        application = mock(TelegramBotApplication.class);
        lifecycle = new TelegramBotLifecycle(application);
    }

    @Test
    void shouldNotBeRunningInitially() {
        assertFalse(lifecycle.isRunning());
    }

    @Test
    void shouldStartApplication() {
        lifecycle.start();

        assertTrue(lifecycle.isRunning());
        verify(application).start();
    }

    @Test
    void shouldNotStartApplicationTwice() {
        lifecycle.start();
        lifecycle.start();

        assertTrue(lifecycle.isRunning());
        verify(application, times(1)).start();
    }

    @Test
    void shouldStopApplication() {
        lifecycle.start();

        lifecycle.stop();

        assertFalse(lifecycle.isRunning());
        verify(application).start();
        verify(application).stop();
    }

    @Test
    void shouldNotStopApplicationWhenNotRunning() {
        lifecycle.stop();

        assertFalse(lifecycle.isRunning());
        verifyNoInteractions(application);
    }

    @Test
    void shouldNotStopApplicationTwice() {
        lifecycle.start();

        lifecycle.stop();
        lifecycle.stop();

        assertFalse(lifecycle.isRunning());
        verify(application).stop();
    }

    @Test
    void shouldResetRunningStateWhenStartFails() {
        RuntimeException exception = new RuntimeException("Failed to start");

        doThrow(exception)
                .when(application)
                .start();

        assertThrows(
                RuntimeException.class,
                () -> lifecycle.start()
        );

        assertFalse(lifecycle.isRunning());
        verify(application).start();
    }

    @Test
    void shouldAllowStartingAgainAfterStartFailure() {
        RuntimeException exception = new RuntimeException("Failed to start");

        doThrow(exception)
                .when(application)
                .start();

        assertThrows(RuntimeException.class, lifecycle::start);

        reset(application);

        lifecycle.start();

        assertTrue(lifecycle.isRunning());
        verify(application).start();
    }

    @Test
    void shouldAlwaysRunCallbackWhenStopping() {
        lifecycle.start();

        Runnable callback = mock(Runnable.class);

        lifecycle.stop(callback);

        assertFalse(lifecycle.isRunning());
        verify(application).stop();
        verify(callback).run();
    }

    @Test
    void shouldRunCallbackEvenWhenStopFails() {
        RuntimeException exception = new RuntimeException("Failed to stop");

        doThrow(exception)
                .when(application)
                .stop();

        lifecycle.start();

        Runnable callback = mock(Runnable.class);

        assertThrows(
                RuntimeException.class,
                () -> lifecycle.stop(callback)
        );

        assertFalse(lifecycle.isRunning());
        verify(callback).run();
    }

    @Test
    void shouldBeAutoStartup() {
        assertTrue(lifecycle.isAutoStartup());
    }

    @Test
    void shouldHaveMaxPhase() {
        assertEquals(Integer.MAX_VALUE, lifecycle.getPhase());
    }
}