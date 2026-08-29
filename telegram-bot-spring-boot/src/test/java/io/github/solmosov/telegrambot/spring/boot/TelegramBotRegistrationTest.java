package io.github.solmosov.telegrambot.spring.boot;

import io.github.solmosov.telegrambot.bot.HandlerRegistrationMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TelegramBotRegistrationTest {

    @Test
    void shouldCreateRegistration() {
        TelegramBotRegistration registration = TelegramBotRegistration.builder()
                .botName("my-bot")
                .token("my-token")
                .build();

        assertEquals("my-bot", registration.botName());
        assertEquals("my-token", registration.token());
        assertNotNull(registration.config());
    }

    @Test
    void shouldUseExternalHandlerRegistrationModeByDefault() {
        TelegramBotRegistration registration = TelegramBotRegistration.builder()
                .botName("my-bot")
                .token("my-token")
                .build();

        assertEquals(
                HandlerRegistrationMode.EXTERNAL,
                registration.config().getHandlerRegistrationMode()
        );
    }

    @Test
    void shouldApplyCustomConfig() {
        TelegramBotRegistration registration = TelegramBotRegistration.builder()
                .botName("my-bot")
                .token("my-token")
                .config(config ->
                        config.handlerRegistrationMode(
                                HandlerRegistrationMode.CLASSPATH_SCAN
                        )
                )
                .build();

        assertEquals(
                HandlerRegistrationMode.CLASSPATH_SCAN,
                registration.config().getHandlerRegistrationMode()
        );
    }

    @Test
    void shouldRejectNullCustomizer() {
        TelegramBotRegistration.Builder builder =
                TelegramBotRegistration.builder();

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> builder.config(null)
        );

        assertEquals(
                "customizer must not be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNullBotName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> TelegramBotRegistration.builder()
                        .token("my-token")
                        .build()
        );

        assertEquals(
                "Bot name must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectBlankBotName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> TelegramBotRegistration.builder()
                        .botName("   ")
                        .token("my-token")
                        .build()
        );

        assertEquals(
                "Bot name must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNullToken() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> TelegramBotRegistration.builder()
                        .botName("my-bot")
                        .build()
        );

        assertEquals(
                "Bot token must not be blank for bot: my-bot",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectBlankToken() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> TelegramBotRegistration.builder()
                        .botName("my-bot")
                        .token("   ")
                        .build()
        );

        assertEquals(
                "Bot token must not be blank for bot: my-bot",
                exception.getMessage()
        );
    }
}