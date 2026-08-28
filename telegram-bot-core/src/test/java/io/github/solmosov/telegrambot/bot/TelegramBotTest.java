package io.github.solmosov.telegrambot.bot;

import io.github.solmosov.telegrambot.exception.TelegramBotException;
import io.github.solmosov.telegrambot.source.UpdateSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TelegramBotTest {

    @Test
    void shouldCreateBotWithName() {
        TelegramBot bot = new TelegramBot(
                "test-bot",
                "fake-token"
        );

        assertEquals("test-bot", bot.name());
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

        assertEquals("test-bot", bot.name());
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
}