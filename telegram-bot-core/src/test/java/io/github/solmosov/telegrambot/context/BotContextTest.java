package io.github.solmosov.telegrambot.context;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.model.CallbackQuery;
import io.github.solmosov.telegrambot.model.Message;
import io.github.solmosov.telegrambot.model.Update;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BotContextTest {

    @Nested
    class ConstructorTests {
        @Test
        void shouldInitializeMessageContextWhenUpdateContainsMessage() {
            // given
            TelegramClient client = mock(TelegramClient.class);
            Message message = mock(Message.class);
            Update update = new Update(1L, message, null);

            // when
            BotContext context = new BotContext(client, update);

            // then
            assertNotNull(context.message());
            assertNotNull(context.photo());
            assertNotNull(context.replyKeyboard());
            assertNull(context.callbackQuery());
        }

        @Test
        void shouldInitializeMessageContextFromCallbackQueryMessage() {
            // given
            TelegramClient client = mock(TelegramClient.class);
            Message message = mock(Message.class);
            CallbackQuery callbackQuery = new CallbackQuery(
                    "callback-id",
                    null,
                    message,
                    "chat-instance",
                    "data"
            );
            Update update = new Update(1L, null, callbackQuery);

            // when
            BotContext context = new BotContext(client, update);

            // then
            assertNotNull(context.message());
            assertNull(context.photo());
            assertNotNull(context.replyKeyboard());
            assertNotNull(context.callbackQuery());
        }

        @Test
        void shouldHaveNullMessageContextWhenUpdateHasNoMessage() {
            // given
            TelegramClient client = mock(TelegramClient.class);
            Update update = new Update(1L, null, null);

            // when
            BotContext context = new BotContext(client, update);

            // then
            assertNull(context.message());
            assertNull(context.photo());
            assertNull(context.callbackQuery());
            assertNotNull(context.replyKeyboard());
        }

    }

    @Nested
    class UpdateTests {
        @Test
        void shouldReturnCurrentUpdate() {
            // given
            TelegramClient client = mock(TelegramClient.class);
            Message message = mock(Message.class);
            Update update = new Update(1L, message, null);

            BotContext context = new BotContext(client, update);

            // when
            Update result = context.update();

            // then
            assertSame(update, result);
        }

        @Test
        void shouldReturnMessageContext() {
            // given
            TelegramClient client = mock(TelegramClient.class);
            Message message = mock(Message.class);
            Update update = new Update(1L, message, null);

            BotContext context = new BotContext(client, update);

            // when
            MessageContext result = context.message();

            // then
            assertNotNull(result);
        }
    }

    @Nested
    class MessageContextTests {
        // message()
        // messageId()
    }

    @Nested
    class ChatActionTests {
        // chatAction()
    }

    @Nested
    class ReplyTests {
        // reply()
    }

    @Nested
    class EditMessageTests {
        // editMessage()
        // editMessageCaption()
    }

    @Nested
    class DeleteMessageTests {
        // deleteMessage()
    }

    @Nested
    class PhotoTests {
        // photo()
        // photo(byte[], ...)
    }

    @Nested
    class VideoTests {
        // video()
        // video(byte[], ...)
    }

    @Nested
    class DocumentTests {
        // document()
        // document(byte[], ...)
    }

    @Nested
    class ReplyKeyboardTests {
        // replyKeyboard()
    }

    @Nested
    class CallbackQueryTests {
        // callbackQuery()
    }

    @Nested
    class DeepLinkTests {
        // setDeepLinkParam()
        // deepLinkParam()
    }
}