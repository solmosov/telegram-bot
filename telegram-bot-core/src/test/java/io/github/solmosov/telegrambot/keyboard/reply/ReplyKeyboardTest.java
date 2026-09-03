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
        ReplyKeyboardMarkup selective =
                ReplyKeyboard.removeKeyboard(true);

        ReplyKeyboardMarkup nonSelective =
                ReplyKeyboard.removeKeyboard(false);

        ReplyKeyboardMarkup unspecified =
                ReplyKeyboard.removeKeyboard(null);

        assertTrue(selective.selective());
        assertFalse(nonSelective.selective());
        assertNull(unspecified.selective());

        assertTrue(selective.removeKeyboard());
        assertTrue(nonSelective.removeKeyboard());
        assertTrue(unspecified.removeKeyboard());
    }

    // --------------------------------------------------
    // button
    // --------------------------------------------------

    @Test
    void button_shouldSetText() {
        ReplyKeyboardButton button =
                ReplyKeyboard.button("Hello");

        JsonNode json = json(button);

        assertEquals(
                objectMapper.valueToTree("Hello"),
                json.path("text")
        );
    }

    @Test
    void button_shouldAllowNullText() {
        ReplyKeyboardButton button =
                ReplyKeyboard.button(null);

        assertFalse(json(button).has("text"));
    }

    @Test
    void buttonLocation_shouldCreateLocationButtonWithText() {
        ReplyKeyboardButton button =
                ReplyKeyboard.buttonLocation("Location");

        JsonNode json = json(button);

        // text field majburiy
        assertEquals(
                objectMapper.valueToTree("Location"),
                json.path("text")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                json.path("request_location")
        );
    }

    @Test
    void buttonContact_shouldSetTextAndRequestContact() {
        ReplyKeyboardButton button =
                ReplyKeyboard.buttonContact("Contact");

        JsonNode json = json(button);

        assertEquals(
                objectMapper.valueToTree("Contact"),
                json.path("text")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                json.path("request_contact")
        );
    }

    // --------------------------------------------------
    // request users
    // --------------------------------------------------

    @Test
    void buttonRequestUsers_shouldSetRequestIdAndText() {
        ReplyKeyboardButton button =
                ReplyKeyboard.buttonRequestUsers("Users", 42);

        JsonNode json = json(button);
        JsonNode requestUsers = json.path("request_users");

        // text field majburiy
        assertEquals(
                objectMapper.valueToTree("Users"),
                json.path("text")
        );

        assertEquals(
                objectMapper.valueToTree(42),
                requestUsers.path("request_id")
        );
    }

    @Test
    void buttonRequestUsers_shouldApplyConsumerConfiguration() {
        ReplyKeyboardButton button =
                ReplyKeyboard.buttonRequestUsers(
                        "Users",
                        42,
                        builder -> builder
                                .userIsBot()
                                .userIsPremium()
                                .requestName()
                                .requestUsername()
                                .requestPhoto()
                                .maxQuantity(10)
                );

        JsonNode json = json(button);
        JsonNode requestUsers = json.path("request_users");

        // text field majburiy
        assertEquals(
                objectMapper.valueToTree("Users"),
                json.path("text")
        );

        assertEquals(
                objectMapper.valueToTree(42),
                requestUsers.path("request_id")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                requestUsers.path("user_is_bot")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                requestUsers.path("user_is_premium")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                requestUsers.path("request_name")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                requestUsers.path("request_username")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                requestUsers.path("request_photo")
        );

        assertEquals(
                objectMapper.valueToTree(10),
                requestUsers.path("max_quantity")
        );
    }

    @Test
    void buttonRequestUsers_shouldExecuteConsumerExactlyOnce() {
        AtomicBoolean called = new AtomicBoolean();

        ReplyKeyboard.buttonRequestUsers(
                "Users",
                1,
                builder -> called.set(true)
        );

        assertTrue(called.get());
    }

    @Test
    void buttonRequestUsers_shouldThrowWhenConsumerIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> ReplyKeyboard.buttonRequestUsers(
                        "Users",
                        1,
                        null
                )
        );
    }

    // --------------------------------------------------
    // row
    // --------------------------------------------------

    @Test
    void row_shouldPreserveButtonOrder() {
        ReplyKeyboardButton first =
                ReplyKeyboard.button("First");

        ReplyKeyboardButton second =
                ReplyKeyboard.button("Second");

        ReplyKeyboardRow row =
                ReplyKeyboard.row(first, second);

        assertEquals(
                List.of(first, second),
                row.buttons()
        );
    }

    @Test
    void row_shouldCreateEmptyRow() {
        ReplyKeyboardRow row =
                ReplyKeyboard.row();

        assertNotNull(row.buttons());
        assertTrue(row.buttons().isEmpty());
    }

    @Test
    void row_shouldThrowWhenButtonIsNull() {
        ReplyKeyboardButton button =
                ReplyKeyboard.button("First");

        assertThrows(
                NullPointerException.class,
                () -> ReplyKeyboard.row(button, null)
        );
    }

    @Test
    void row_shouldThrowWhenButtonsArrayIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> ReplyKeyboard.row(
                        (ReplyKeyboardButton[]) null
                )
        );
    }

    // --------------------------------------------------
    // of
    // --------------------------------------------------

    @Test
    void of_shouldPutEachButtonIntoSeparateRow() {
        ReplyKeyboardButton first =
                ReplyKeyboard.button("First");

        ReplyKeyboardButton second =
                ReplyKeyboard.button("Second");

        ReplyKeyboardMarkup markup =
                ReplyKeyboard.of(first, second);

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
        ReplyKeyboardButton first =
                ReplyKeyboard.button("First");

        ReplyKeyboardButton second =
                ReplyKeyboard.button("Second");

        ReplyKeyboardRow row =
                ReplyKeyboard.row(first, second);

        ReplyKeyboardMarkup markup =
                ReplyKeyboard.of(row);

        assertEquals(
                List.of(
                        List.of(first, second)
                ),
                markup.keyboard()
        );
    }

    @Test
    void of_shouldPreserveMixedElementOrder() {
        ReplyKeyboardButton first =
                ReplyKeyboard.button("First");

        ReplyKeyboardButton second =
                ReplyKeyboard.button("Second");

        ReplyKeyboardButton third =
                ReplyKeyboard.button("Third");

        ReplyKeyboardRow row =
                ReplyKeyboard.row(second, third);

        ReplyKeyboardMarkup markup =
                ReplyKeyboard.of(first, row);

        assertEquals(
                List.of(
                        List.of(first),
                        List.of(second, third)
                ),
                markup.keyboard()
        );
    }

    @Test
    void of_shouldPreserveButtonIdentity() {
        ReplyKeyboardButton button =
                ReplyKeyboard.button("Test");

        ReplyKeyboardMarkup markup =
                ReplyKeyboard.of(button);

        assertSame(
                button,
                markup.keyboard().get(0).get(0)
        );
    }

    @Test
    void of_shouldCreateEmptyKeyboard() {
        ReplyKeyboardMarkup markup =
                ReplyKeyboard.of();

        assertNotNull(markup.keyboard());
        assertTrue(markup.keyboard().isEmpty());
        assertTrue(markup.resizeKeyboard());
    }

    @Test
    void of_shouldIgnoreNullElements() {
        ReplyKeyboardButton button =
                ReplyKeyboard.button("Valid");

        ReplyKeyboardMarkup markup =
                ReplyKeyboard.of(
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
    void of_shouldThrowWhenElementsArrayIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> ReplyKeyboard.of(
                        (ReplyKeyboardElement[]) null
                )
        );
    }

    // --------------------------------------------------
    // integration
    // --------------------------------------------------

    @Test
    void of_shouldSupportAllButtonTypes() {
        ReplyKeyboardButton text =
                ReplyKeyboard.button("Text");

        ReplyKeyboardButton location =
                ReplyKeyboard.buttonLocation("Location");

        ReplyKeyboardButton contact =
                ReplyKeyboard.buttonContact("Contact");

        ReplyKeyboardButton users =
                ReplyKeyboard.buttonRequestUsers("Users", 100);

        ReplyKeyboardMarkup markup =
                ReplyKeyboard.of(
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

    private JsonNode json(Object value) {
        return objectMapper.valueToTree(value);
    }
}