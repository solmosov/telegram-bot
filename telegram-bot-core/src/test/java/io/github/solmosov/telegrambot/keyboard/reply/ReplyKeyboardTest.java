package io.github.solmosov.telegrambot.keyboard.reply;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReplyKeyboardTest {

    @Test
    void shouldCreateButton() {
        ReplyKeyboardButton button = ReplyKeyboard.button("Hello");

        assertEquals("Hello", button.text());
        assertNull(button.requestLocation());
        assertNull(button.requestContact());
        assertNull(button.requestUsers());
    }

    @Test
    void shouldCreateLocationButton() {
        ReplyKeyboardButton button = ReplyKeyboard.buttonLocation("Send location");

        assertEquals("Send location", button.text());
        assertEquals(true, button.requestLocation());
        assertNull(button.requestContact());
        assertNull(button.requestUsers());
    }

    @Test
    void shouldCreateContactButton() {
        ReplyKeyboardButton button = ReplyKeyboard.buttonContact("Share contact");

        assertEquals("Share contact", button.text());
        assertNull(button.requestLocation());
        assertEquals(true, button.requestContact());
        assertNull(button.requestUsers());
    }

    @Test
    void shouldCreateRequestUsersButton() {
        ReplyKeyboardButton button = ReplyKeyboard.buttonRequestUsers(
                "Select users",
                42,
                builder -> builder
                        .userIsBot()
                        .requestName()
                        .requestUsername()
                        .requestPhoto()
                        .userIsPremium()
                        .maxQuantity(5)
        );

        assertEquals("Select users", button.text());
        assertNull(button.requestLocation());
        assertNull(button.requestContact());

        assertNotNull(button.requestUsers());

        // RequestUsers fields are private, so the important part here
        // is that the button contains the configured RequestUsers object.
        assertEquals(42, getRequestId(button.requestUsers()));
    }

    @Test
    void shouldCreateRow() {
        ReplyKeyboardButton first = ReplyKeyboard.button("One");
        ReplyKeyboardButton second = ReplyKeyboard.button("Two");

        ReplyKeyboardRow row = ReplyKeyboard.row(first, second);

        assertEquals(List.of(first, second), row.buttons());
    }

    @Test
    void shouldCreateKeyboardFromButtonsAndRows() {
        ReplyKeyboardButton first = ReplyKeyboard.button("First");
        ReplyKeyboardButton second = ReplyKeyboard.button("Second");
        ReplyKeyboardButton third = ReplyKeyboard.button("Third");

        ReplyKeyboardRow row = ReplyKeyboard.row(second, third);

        ReplyKeyboardMarkup keyboard = ReplyKeyboard.of(
                first,
                row
        );

        assertEquals(
                List.of(
                        List.of(first),
                        List.of(second, third)
                ),
                keyboard.keyboard()
        );

        assertEquals(true, keyboard.resizeKeyboard());
        assertEquals(true, keyboard.oneTimeKeyboard());
        assertNull(keyboard.remove_keyboard());
    }

    @Test
    void shouldCreateKeyboardWithMultipleButtonRows() {
        ReplyKeyboardButton first = ReplyKeyboard.button("First");
        ReplyKeyboardButton second = ReplyKeyboard.button("Second");
        ReplyKeyboardButton third = ReplyKeyboard.button("Third");

        ReplyKeyboardMarkup keyboard = ReplyKeyboard.of(
                first,
                second,
                third
        );

        assertEquals(
                List.of(
                        List.of(first),
                        List.of(second),
                        List.of(third)
                ),
                keyboard.keyboard()
        );
    }

    @Test
    void shouldBuildKeyboardWithDefaultValues() {
        ReplyKeyboardButton button = ReplyKeyboard.button("Hello");

        ReplyKeyboardMarkup keyboard = ReplyKeyboard.builder()
                .of(button)
                .build();

        assertEquals(List.of(List.of(button)), keyboard.keyboard());
        assertEquals(false, keyboard.resizeKeyboard());
        assertEquals(false, keyboard.oneTimeKeyboard());
        assertEquals(false, keyboard.remove_keyboard());
    }

    @Test
    void shouldBuildKeyboardWithConfiguredOptions() {
        ReplyKeyboardButton button = ReplyKeyboard.button("Hello");

        ReplyKeyboardMarkup keyboard = ReplyKeyboard.builder()
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .removeKeyboard(true)
                .of(button)
                .build();

        assertEquals(List.of(List.of(button)), keyboard.keyboard());
        assertTrue(keyboard.resizeKeyboard());
        assertTrue(keyboard.oneTimeKeyboard());
        assertTrue(keyboard.remove_keyboard());
    }

    @Test
    void shouldBuildKeyboardWithRows() {
        ReplyKeyboardButton first = ReplyKeyboard.button("First");
        ReplyKeyboardButton second = ReplyKeyboard.button("Second");
        ReplyKeyboardButton third = ReplyKeyboard.button("Third");

        ReplyKeyboardRow row = ReplyKeyboard.row(second, third);

        ReplyKeyboardMarkup keyboard = ReplyKeyboard.builder()
                .of(first, row)
                .build();

        assertEquals(
                List.of(
                        List.of(first),
                        List.of(second, third)
                ),
                keyboard.keyboard()
        );
    }

    @Test
    void shouldRemoveKeyboard() {
        ReplyKeyboardMarkup keyboard = ReplyKeyboard.removeKeyboard();

        assertNull(keyboard.keyboard());
        assertNull(keyboard.resizeKeyboard());
        assertNull(keyboard.oneTimeKeyboard());
        assertTrue(keyboard.remove_keyboard());
    }

    @Test
    void shouldAllowEmptyKeyboard() {
        ReplyKeyboardMarkup keyboard = ReplyKeyboard.of();

        assertNotNull(keyboard);
        assertTrue(keyboard.keyboard().isEmpty());
        assertEquals(true, keyboard.resizeKeyboard());
        assertEquals(true, keyboard.oneTimeKeyboard());
        assertNull(keyboard.remove_keyboard());
    }

    @Test
    void shouldAllowEmptyBuilderKeyboard() {
        ReplyKeyboardMarkup keyboard = ReplyKeyboard.builder()
                .build();

        assertNotNull(keyboard);
        assertTrue(keyboard.keyboard().isEmpty());
        assertFalse(keyboard.resizeKeyboard());
        assertFalse(keyboard.oneTimeKeyboard());
        assertFalse(keyboard.remove_keyboard());
    }

    private static int getRequestId(RequestUsers requestUsers) {
        try {
            var field = RequestUsers.class.getDeclaredField("requestId");
            field.setAccessible(true);
            return field.getInt(requestUsers);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Could not read requestId", e);
        }
    }
}
