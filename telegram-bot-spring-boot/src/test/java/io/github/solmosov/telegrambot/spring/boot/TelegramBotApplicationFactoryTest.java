package io.github.solmosov.telegrambot.spring.boot;

import io.github.solmosov.telegrambot.bot.TelegramBotApplication;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TelegramBotApplicationFactoryTest {

    @Test
    void shouldCreateApplicationWithRegisteredBots() {
        TelegramBotRegistration first = TelegramBotRegistration.builder().botName("first-bot").token("first-token").build();
        TelegramBotRegistration second = TelegramBotRegistration.builder().botName("second-bot").token("second-token").build();
        TelegramBotApplicationFactory factory = new TelegramBotApplicationFactory(List.of(first, second));
        TelegramBotApplication application = factory.create();
        assertNotNull(application);
        assertTrue(application.containsBot("first-bot"));
        assertTrue(application.containsBot("second-bot"));
        assertEquals(2, application.bots().size());
    }

    @Test
    void shouldCreateEmptyApplicationWhenThereAreNoRegistrations() {
        TelegramBotApplicationFactory factory = new TelegramBotApplicationFactory(List.of());
        TelegramBotApplication application = factory.create();
        assertNotNull(application);
        assertTrue(application.bots().isEmpty());
    }

    @Test
    void shouldRegisterBotWithCorrectNameAndToken() {
        TelegramBotRegistration registration = TelegramBotRegistration.builder().botName("my-bot").token("my-token").build();
        TelegramBotApplicationFactory factory = new TelegramBotApplicationFactory(List.of(registration));
        TelegramBotApplication application = factory.create();
        assertTrue(application.containsBot("my-bot"));
        assertDoesNotThrow(() -> application.getBot("my-bot"));
        assertEquals("my-bot", application.getBot("my-bot").getName());
        assertEquals("my-token", application.getBot("my-bot").getToken());
    }

}