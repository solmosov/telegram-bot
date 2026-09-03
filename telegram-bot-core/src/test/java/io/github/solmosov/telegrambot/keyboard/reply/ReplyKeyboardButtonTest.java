package io.github.solmosov.telegrambot.keyboard.reply;

import io.github.solmosov.telegrambot.json.ObjectMapperFactory;
import io.github.solmosov.telegrambot.keyboard.reply.button.RequestUsers;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

class ReplyKeyboardButtonTest {

    private final ObjectMapper objectMapper = ObjectMapperFactory.create();

    @Test
    void builder_shouldCreateEmptyButton() {
        ReplyKeyboardButton button = ReplyKeyboardButton.builder()
                .build();

        JsonNode json = json(button);

        assertTrue(json.isEmpty());
    }

    @Test
    void text_shouldBeSerialized() {
        ReplyKeyboardButton button = ReplyKeyboardButton.builder()
                .text("Hello")
                .build();

        JsonNode json = json(button);

        assertEquals(
                objectMapper.valueToTree("Hello"),
                json.path("text")
        );
    }

    @Test
    void nullText_shouldBeExcludedFromJson() {
        ReplyKeyboardButton button = ReplyKeyboardButton.builder()
                .text(null)
                .build();

        JsonNode json = json(button);

        assertFalse(json.has("text"));
    }

    @Test
    void style_shouldSerializeTelegramValue() {
        ReplyKeyboardButton danger = ReplyKeyboardButton.builder()
                .style(ReplyKeyboardButton.Style.DANGER)
                .build();

        ReplyKeyboardButton success = ReplyKeyboardButton.builder()
                .style(ReplyKeyboardButton.Style.SUCCESS)
                .build();

        ReplyKeyboardButton primary = ReplyKeyboardButton.builder()
                .style(ReplyKeyboardButton.Style.PRIMARY)
                .build();

        assertEquals(
                objectMapper.valueToTree("danger"),
                json(danger).path("style")
        );

        assertEquals(
                objectMapper.valueToTree("success"),
                json(success).path("style")
        );

        assertEquals(
                objectMapper.valueToTree("primary"),
                json(primary).path("style")
        );
    }

    @Test
    void requestLocation_shouldSetRequestLocationToTrue() {
        ReplyKeyboardButton button = ReplyKeyboardButton.builder()
                .text("Location")
                .requestLocation()
                .build();

        JsonNode json = json(button);

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
    void requestContact_shouldSetRequestContactToTrue() {
        ReplyKeyboardButton button = ReplyKeyboardButton.builder()
                .text("Contact")
                .requestContact()
                .build();

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

    @Test
    void requestUsers_shouldBeSerialized() {
        RequestUsers requestUsers = RequestUsers.builder(42)
                .userIsBot()
                .userIsPremium()
                .maxQuantity(5)
                .build();

        ReplyKeyboardButton button = ReplyKeyboardButton.builder()
                .text("Users")
                .requestUsers(requestUsers)
                .build();

        JsonNode json = json(button);
        JsonNode users = json.path("request_users");

        assertEquals(
                objectMapper.valueToTree("Users"),
                json.path("text")
        );

        assertEquals(
                objectMapper.valueToTree(42),
                users.path("request_id")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                users.path("user_is_bot")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                users.path("user_is_premium")
        );

        assertEquals(
                objectMapper.valueToTree(5),
                users.path("max_quantity")
        );
    }

    @Test
    void nullRequestUsers_shouldBeExcludedFromJson() {
        ReplyKeyboardButton button = ReplyKeyboardButton.builder()
                .requestUsers(null)
                .build();

        assertFalse(json(button).has("request_users"));
    }

    @Test
    void webApp_shouldBeSerialized() {
        ReplyKeyboardButton button = ReplyKeyboardButton.builder()
                .text("Open")
                .webApp("https://example.com")
                .build();

        JsonNode json = json(button);

        assertEquals(
                objectMapper.valueToTree("Open"),
                json.path("text")
        );

        assertEquals(
                objectMapper.valueToTree("https://example.com"),
                json.path("web_app").path("url")
        );
    }

    @Test
    void webApp_withNullUrl_shouldStillCreateWebAppObject() {
        ReplyKeyboardButton button = ReplyKeyboardButton.builder()
                .webApp(null)
                .build();

        JsonNode json = json(button);

        System.out.println(json);
        assertTrue(json.has("web_app"));
        assertTrue(json.get("web_app").isObject());
        assertFalse(json.get("web_app").isEmpty());
    }

    @Test
    void builder_shouldSupportAllOptionsTogether() {
        RequestUsers requestUsers = RequestUsers.builder(100)
                .userIsBot()
                .userIsPremium()
                .requestName()
                .requestUsername()
                .requestPhoto()
                .maxQuantity(10)
                .build();

        ReplyKeyboardButton button = ReplyKeyboardButton.builder()
                .text("Everything")
                .style(ReplyKeyboardButton.Style.PRIMARY)
                .requestLocation()
                .requestContact()
                .requestUsers(requestUsers)
                .webApp("https://example.com")
                .build();

        JsonNode json = json(button);
        JsonNode users = json.path("request_users");

        assertEquals(
                objectMapper.valueToTree("Everything"),
                json.path("text")
        );

        assertEquals(
                objectMapper.valueToTree("primary"),
                json.path("style")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                json.path("request_location")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                json.path("request_contact")
        );

        assertEquals(
                objectMapper.valueToTree(100),
                users.path("request_id")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                users.path("user_is_bot")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                users.path("user_is_premium")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                users.path("request_name")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                users.path("request_username")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                users.path("request_photo")
        );

        assertEquals(
                objectMapper.valueToTree(10),
                users.path("max_quantity")
        );

        assertEquals(
                objectMapper.valueToTree("https://example.com"),
                json.path("web_app").path("url")
        );
    }

    @Test
    void builder_shouldOverridePreviousText() {
        ReplyKeyboardButton button = ReplyKeyboardButton.builder()
                .text("First")
                .text("Second")
                .build();

        assertEquals(
                objectMapper.valueToTree("Second"),
                json(button).path("text")
        );
    }

    @Test
    void builder_shouldOverridePreviousStyle() {
        ReplyKeyboardButton button = ReplyKeyboardButton.builder()
                .style(ReplyKeyboardButton.Style.DANGER)
                .style(ReplyKeyboardButton.Style.SUCCESS)
                .build();

        assertEquals(
                objectMapper.valueToTree("success"),
                json(button).path("style")
        );
    }

    @Test
    void repeatedRequestLocation_shouldRemainTrue() {
        ReplyKeyboardButton button = ReplyKeyboardButton.builder()
                .requestLocation()
                .requestLocation()
                .build();

        assertEquals(
                objectMapper.valueToTree(true),
                json(button).path("request_location")
        );
    }

    @Test
    void repeatedRequestContact_shouldRemainTrue() {
        ReplyKeyboardButton button = ReplyKeyboardButton.builder()
                .requestContact()
                .requestContact()
                .build();

        assertEquals(
                objectMapper.valueToTree(true),
                json(button).path("request_contact")
        );
    }

    @Test
    void nullableFields_shouldNotBeSerialized() {
        ReplyKeyboardButton button = ReplyKeyboardButton.builder()
                .build();

        JsonNode json = json(button);

        assertFalse(json.has("text"));
        assertFalse(json.has("style"));
        assertFalse(json.has("request_users"));
        assertFalse(json.has("request_contact"));
        assertFalse(json.has("request_location"));
        assertFalse(json.has("web_app"));
    }

    @Test
    void builder_shouldBeReusableButKeepItsState() {
        ReplyKeyboardButton.Builder builder =
                ReplyKeyboardButton.builder();

        ReplyKeyboardButton first = builder
                .text("First")
                .build();

        ReplyKeyboardButton second = builder
                .text("Second")
                .requestContact()
                .build();

        assertEquals(
                objectMapper.valueToTree("First"),
                json(first).path("text")
        );

        assertFalse(json(first).has("request_contact"));

        assertEquals(
                objectMapper.valueToTree("Second"),
                json(second).path("text")
        );

        assertEquals(
                objectMapper.valueToTree(true),
                json(second).path("request_contact")
        );
    }

    private JsonNode json(Object value) {
        try {
            return objectMapper.valueToTree(value);
        } catch (IllegalArgumentException e) {
            fail("Could not serialize object to JSON", e);
            return null;
        }
    }
}