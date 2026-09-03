package io.github.solmosov.telegrambot.keyboard.reply;

import io.github.solmosov.telegrambot.json.ObjectMapperFactory;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ReplyKeyboardTest {

    private final ObjectMapper objectMapper = ObjectMapperFactory.create();

    // --------------------------------------------------
    // removeKeyboard
    // --------------------------------------------------

    @Test
    void removeKeyboard_shouldCreateRemoveMarkup() {
        ReplyKeyboardMarkup markup = ReplyKeyboard.removeKeyboard();

        assertNull(markup.keyboard());
        assertNull(markup.isPersistent());
        assertNull(markup.resizeKeyboard());
        assertNull(markup.oneTimeKeyboard());
        assertNull(markup.inputFieldPlaceholder());
        assertNull(markup.selective());
        assertTrue(markup.removeKeyboard());
    }

    @Test
    void removeKeyboard_shouldPreserveSelectiveValue() {
        ReplyKeyboardMarkup selective = ReplyKeyboard.removeKeyboard(true);
        ReplyKeyboardMarkup nonSelective = ReplyKeyboard.removeKeyboard(false);
        ReplyKeyboardMarkup unspecified = ReplyKeyboard.removeKeyboard(null);

        assertTrue(selective.selective());
        assertFalse(nonSelective.selective());
        assertNull(unspecified.selective());

        assertTrue(selective.removeKeyboard());
        assertTrue(nonSelective.removeKeyboard());
        assertTrue(unspecified.removeKeyboard());
    }

    // --------------------------------------------------
    // buttons
    // --------------------------------------------------

    @Test
    void button_shouldSetText() {
        ReplyKeyboardButton button = ReplyKeyboard.button("Hello");

        assertEquals("Hello", json(button).get("text").asText());
        assertEquals(1, json(button).size());
    }

    @Test
    void button_shouldAllowNullText() {
        ReplyKeyboardButton button = ReplyKeyboard.button(null);

        assertNull(json(button).get("text"));
    }

    @Test
    void buttonLocation_shouldCreateLocationButton() {
        ReplyKeyboardButton button = ReplyKeyboard.buttonLocation("ignored");

        JsonNode json = json(button);

        assertTrue(json.get("request_location").asBoolean());
        assertNull(json.get("text"));
    }

    @Test
    void buttonContact_shouldSetTextAndRequestContact() {
        ReplyKeyboardButton button = ReplyKeyboard.buttonContact("Share contact");

        JsonNode json = json(button);

        assertEquals("Share contact", json.get("text").asText());
        assertTrue(json.get("request_contact").asBoolean());
    }

    @Test
    void buttonRequestUsers_shouldSetRequestId() {
        ReplyKeyboardButton button =
                ReplyKeyboard.buttonRequestUsers("Select users", 42);

        JsonNode json = json(button);

        assertEquals("Select users", json.get("text").asText());
        assertEquals(42, json.get("request_users").get("request_id").asInt());
    }

    @Test
    void buttonRequestUsers_shouldApplyConsumerConfiguration() {
        ReplyKeyboardButton button = ReplyKeyboard.buttonRequestUsers(
                "Select users",
                42,
                builder -> builder
                        .userIsBot()
                        .userIsPremium()
                        .requestName()
                        .requestUsername()
                        .requestPhoto()
                        .maxQuantity(10)
        );

        JsonNode requestUsers = json(button).get("request_users");

        assertEquals(42, requestUsers.get("request_id").asInt());
        assertTrue(requestUsers.get("user_is_bot").asBoolean());
        assertTrue(requestUsers.get("user_is_premium").asBoolean());
        assertTrue(requestUsers.get("request_name").asBoolean());
        assertTrue(requestUsers.get("request_username").asBoolean());
        assertTrue(requestUsers.get("request_photo").asBoolean());
        assertEquals(10, requestUsers.get("max_quantity").asInt());
    }

    @Test
    void buttonRequestUsers_shouldExecuteConsumerExactlyOnce() {
        AtomicBoolean called = new AtomicBoolean(false);

        ReplyKeyboard.buttonRequestUsers(
                "Users",
                1,
                builder -> called.set(true)
        );

        assertTrue(called.get());
    }

    @Test
    void buttonRequestUsers_shouldRejectNullConsumer() {
        assertThrows(
                NullPointerException.class,
                () -> ReplyKeyboard.buttonRequestUsers("Users", 1, null)
        );
    }

    // --------------------------------------------------
    // row
    // --------------------------------------------------

    @Test
    void row_shouldPreserveButtonOrder() {
        ReplyKeyboardButton first = ReplyKeyboard.button("First");
        ReplyKeyboardButton second = ReplyKeyboard.button("Second");

        ReplyKeyboardRow row = ReplyKeyboard.row(first, second);

        assertEquals(List.of(first, second), row.buttons());
    }

    @Test
    void row_shouldCreateEmptyRowWhenNoButtonsProvided() {
        ReplyKeyboardRow row = ReplyKeyboard.row();

        assertNotNull(row.buttons());
        assertTrue(row.buttons().isEmpty());
    }

    @Test
    void row_shouldThrowWhenButtonIsNull() {
        ReplyKeyboardButton button = ReplyKeyboard.button("First");

        assertThrows(
                NullPointerException.class,
                () -> ReplyKeyboard.row(button, null)
        );
    }

    @Test
    void row_shouldThrowWhenVarargsArrayItselfIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> ReplyKeyboard.row((ReplyKeyboardButton[]) null)
        );
    }

    // --------------------------------------------------
    // of
    // --------------------------------------------------

    @Test
    void of_shouldPutEachButtonIntoSeparateRow() {
        ReplyKeyboardButton first = ReplyKeyboard.button("First");
        ReplyKeyboardButton second = ReplyKeyboard.button("Second");

        ReplyKeyboardMarkup markup = ReplyKeyboard.of(first, second);

        assertEquals(
                List.of(
                        List.of(first),
                        List.of(second)
                ),
                markup.keyboard()
        );

        assertTrue(markup.resizeKeyboard());
    }

    @Test
    void of_shouldKeepRowAsSingleKeyboardRow() {
        ReplyKeyboardButton first = ReplyKeyboard.button("First");
        ReplyKeyboardButton second = ReplyKeyboard.button("Second");

        ReplyKeyboardRow row = ReplyKeyboard.row(first, second);

        ReplyKeyboardMarkup markup = ReplyKeyboard.of(row);

        assertEquals(
                List.of(List.of(first, second)),
                markup.keyboard()
        );
    }

    @Test
    void of_shouldPreserveMixedElementOrder() {
        ReplyKeyboardButton first = ReplyKeyboard.button("First");
        ReplyKeyboardButton second = ReplyKeyboard.button("Second");
        ReplyKeyboardButton third = ReplyKeyboard.button("Third");

        ReplyKeyboardRow row = ReplyKeyboard.row(second, third);

        ReplyKeyboardMarkup markup = ReplyKeyboard.of(
                first,
                row
        );

        assertEquals(
                List.of(
                        List.of(first),
                        List.of(second, third)
                ),
                markup.keyboard()
        );
    }

    @Test
    void of_shouldPreserveSameButtonInstance() {
        ReplyKeyboardButton button = ReplyKeyboard.button("Test");

        ReplyKeyboardMarkup markup = ReplyKeyboard.of(button);

        assertSame(button, markup.keyboard().get(0).get(0));
    }

    @Test
    void of_shouldCreateEmptyKeyboardWithoutElements() {
        ReplyKeyboardMarkup markup = ReplyKeyboard.of();

        assertNotNull(markup.keyboard());
        assertTrue(markup.keyboard().isEmpty());
        assertTrue(markup.resizeKeyboard());
    }

    @Test
    void of_shouldIgnoreNullElements() {
        ReplyKeyboardButton button = ReplyKeyboard.button("Valid");

        ReplyKeyboardMarkup markup = ReplyKeyboard.of(
                null,
                button,
                null
        );

        assertEquals(
                List.of(List.of(button)),
                markup.keyboard()
        );
    }

    @Test
    void of_shouldThrowWhenElementsArrayItselfIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> ReplyKeyboard.of((ReplyKeyboardElement[]) null)
        );
    }

    // --------------------------------------------------
    // integration-style behavior
    // --------------------------------------------------

    @Test
    void of_shouldWorkWithAllSupportedButtonTypes() {
        ReplyKeyboardButton text = ReplyKeyboard.button("Text");
        ReplyKeyboardButton location = ReplyKeyboard.buttonLocation("Location");
        ReplyKeyboardButton contact = ReplyKeyboard.buttonContact("Contact");
        ReplyKeyboardButton users =
                ReplyKeyboard.buttonRequestUsers("Users", 100);

        ReplyKeyboardMarkup markup = ReplyKeyboard.of(
                text,
                ReplyKeyboard.row(location, contact),
                users
        );

        assertEquals(3, markup.keyboard().size());

        assertEquals(
                List.of(text),
                markup.keyboard().get(0)
        );

        assertEquals(
                List.of(location, contact),
                markup.keyboard().get(1)
        );

        assertEquals(
                List.of(users),
                markup.keyboard().get(2)
        );
    }

    // --------------------------------------------------
    // Helpers
    // --------------------------------------------------

    private JsonNode json(Object value) {
        try {
            return objectMapper.valueToTree(value);
        } catch (IllegalArgumentException e) {
            fail("Could not serialize object to JSON", e);
            return null;
        }
    }
}
