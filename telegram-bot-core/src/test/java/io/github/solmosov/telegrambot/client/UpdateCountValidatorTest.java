package io.github.solmosov.telegrambot.client;

import io.github.solmosov.telegrambot.exception.client.TelegramClientException;
import io.github.solmosov.telegrambot.model.Update;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UpdateCountValidatorTest {

    @Test
    void shouldNotThrowWhenUpdatesIsNull() {
        UpdateCountValidator validator = new UpdateCountValidator(10);

        assertDoesNotThrow(() -> validator.validate(null));
    }

    @Test
    void shouldNotThrowWhenUpdatesIsEmpty() {
        UpdateCountValidator validator = new UpdateCountValidator(10);

        assertDoesNotThrow(() -> validator.validate(List.of()));
    }

    @Test
    void shouldNotThrowWhenUpdateCountIsLessThanMax() {
        UpdateCountValidator validator = new UpdateCountValidator(3);
        List<Update> updates = Collections.nCopies(2, null);

        assertDoesNotThrow(() -> validator.validate(updates));
    }

    @Test
    void shouldNotThrowWhenUpdateCountEqualsMax() {
        UpdateCountValidator validator = new UpdateCountValidator(2);
        List<Update> updates = Collections.nCopies(2, null);

        assertDoesNotThrow(() -> validator.validate(updates));
    }

    @Test
    void shouldThrowWhenUpdateCountExceedsMax() {
        UpdateCountValidator validator = new UpdateCountValidator(2);
        List<Update> updates = Collections.nCopies(3, null);

        TelegramClientException exception = assertThrows(
                TelegramClientException.class,
                () -> validator.validate(updates)
        );

        assertEquals(
                "Too many updates received: 3 (max: 2)",
                exception.getMessage()
        );
    }
}