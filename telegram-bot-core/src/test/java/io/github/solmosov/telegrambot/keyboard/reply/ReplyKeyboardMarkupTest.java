package io.github.solmosov.telegrambot.keyboard.reply;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReplyKeyboardMarkupTest {

    @Test
    void initial_shouldCreateMarkupWithKeyboardAndResizeEnabled() {
        ReplyKeyboardButton button = ReplyKeyboardButton.builder()
                .text("Test")
                .build();

        List<List<ReplyKeyboardButton>> keyboard = List.of(
                List.of(button)
        );

        ReplyKeyboardMarkup markup = ReplyKeyboardMarkup.initial(keyboard);

        assertEquals(keyboard, markup.keyboard());
        assertTrue(markup.resizeKeyboard());
        assertNull(markup.isPersistent());
        assertNull(markup.oneTimeKeyboard());
        assertNull(markup.inputFieldPlaceholder());
        assertNull(markup.selective());
        assertNull(markup.removeKeyboard());
    }

    @Test
    void remove_shouldCreateRemoveKeyboardMarkup() {
        ReplyKeyboardMarkup markup = ReplyKeyboardMarkup.remove(true);

        assertNull(markup.keyboard());
        assertTrue(markup.removeKeyboard());
        assertTrue(markup.selective());

        assertNull(markup.isPersistent());
        assertNull(markup.resizeKeyboard());
        assertNull(markup.oneTimeKeyboard());
        assertNull(markup.inputFieldPlaceholder());
    }

    @Test
    void remove_withFalseSelective_shouldKeepSelectiveFalse() {
        ReplyKeyboardMarkup markup = ReplyKeyboardMarkup.remove(false);

        assertTrue(markup.removeKeyboard());
        assertFalse(markup.selective());
    }

    @Test
    void options_shouldApplyAllOptions() {
        List<List<ReplyKeyboardButton>> keyboard = List.of(
                List.of(
                        ReplyKeyboardButton.builder()
                                .text("Test")
                                .build()
                )
        );

        ReplyKeyboardMarkup markup = ReplyKeyboardMarkup.initial(keyboard)
                .options(options -> options
                        .isPersistent()
                        .resizeKeyboard()
                        .oneTimeKeyboard()
                        .inputFieldPlaceholder("Enter text")
                        .selective()
                );

        assertEquals(keyboard, markup.keyboard());
        assertTrue(markup.isPersistent());
        assertTrue(markup.resizeKeyboard());
        assertTrue(markup.oneTimeKeyboard());
        assertEquals("Enter text", markup.inputFieldPlaceholder());
        assertTrue(markup.selective());
        assertNull(markup.removeKeyboard());
    }

    @Test
    void options_withoutOptions_shouldKeepResizeEnabled() {
        ReplyKeyboardMarkup markup = ReplyKeyboardMarkup.initial(List.of())
                .options(options -> {});

        assertTrue(markup.resizeKeyboard());
    }
}