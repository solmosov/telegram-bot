package io.github.solmosov.telegrambot.context;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.messaging.builder.*;
import io.github.solmosov.telegrambot.model.CallbackQuery;
import io.github.solmosov.telegrambot.model.Chat;
import io.github.solmosov.telegrambot.model.Message;
import io.github.solmosov.telegrambot.model.Update;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        @Test
        void shouldReturnMessageId() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.messageId()).thenReturn(123L);

            Update update = new Update(
                    1L,
                    message,
                    null
            );

            BotContext context = new BotContext(client, update);

            // when
            long result = context.messageId();

            // then
            assertEquals(123L, result);
        }
    }

    @Nested
    class ChatActionTests {
        @Test
        void shouldCreateChatActionBuilder() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(
                            100L,
                            null,
                            null,
                            null,
                            null,
                            null
                    )
            );

            Update update = new Update(
                    123L,
                    message,
                    null
            );

            BotContext context = new BotContext(client, update);

            // when
            ChatActionBuilder result = context.chatAction();

            // then
            assertNotNull(result);
        }
    }

    @Nested
    class ReplyTests {
        @Test
        void shouldCreateMessageBuilder() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(
                            100L,
                            null,
                            null,
                            null,
                            null,
                            null
                    )
            );

            Update update = new Update(
                    123L,
                    message,
                    null
            );

            BotContext context = new BotContext(client, update);

            // when
            MessageBuilder result = context.reply("Hello");

            // then
            assertNotNull(result);
        }
    }

    @Nested
    class EditMessageTests {
        @Test
        void shouldCreateEditMessageTextBuilder() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(
                            100L,
                            null,
                            null,
                            null,
                            null,
                            null
                    )
            );

            Update update = new Update(
                    123L,
                    message,
                    null
            );

            BotContext context = new BotContext(client, update);

            // when
            EditMessageTextBuilder result =
                    context.editMessage(456L, "Updated text");

            // then
            assertNotNull(result);
        }

        @Test
        void shouldCreateEditMessageCaptionBuilder() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(
                            100L,
                            null,
                            null,
                            null,
                            null,
                            null
                    )
            );

            Update update = new Update(
                    123L,
                    message,
                    null
            );

            BotContext context = new BotContext(client, update);

            // when
            EditMessageCaptionBuilder result =
                    context.editMessageCaption(456L, "Updated caption");

            // then
            assertNotNull(result);
        }
    }

    @Nested
    class DeleteMessageTests {
        @Test
        void shouldCreateDeleteMessageBuilder() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(
                            100L,
                            null,
                            null,
                            null,
                            null,
                            null
                    )
            );

            Update update = new Update(
                    123L,
                    message,
                    null
            );

            BotContext context = new BotContext(client, update);

            // when
            DeleteMessageBuilder result =
                    context.deleteMessage(456L);

            // then
            assertNotNull(result);
        }
    }

    @Nested
    class PhotoTests {

        @Test
        void shouldCreatePhotoBuilder() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(100L, null, null, null, null, null)
            );

            Update update = new Update(123L, message, null);
            BotContext context = new BotContext(client, update);

            // when
            PhotoBuilder result = context.photo("https://example.com/photo.jpg");

            // then
            assertNotNull(result);
        }

        @Test
        void shouldCreatePhotoUploadBuilder() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(100L, null, null, null, null, null)
            );

            Update update = new Update(123L, message, null);
            BotContext context = new BotContext(client, update);

            byte[] file = "image".getBytes();

            // when
            PhotoUploadBuilder result =
                    context.photo(file, "photo.jpg", "image/jpeg");

            // then
            assertNotNull(result);
        }
    }

    @Nested
    class VideoTests {

        @Test
        void shouldCreateVideoBuilder() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(100L, null, null, null, null, null)
            );

            Update update = new Update(123L, message, null);
            BotContext context = new BotContext(client, update);

            // when
            VideoBuilder result =
                    context.video("https://example.com/video.mp4");

            // then
            assertNotNull(result);
        }

        @Test
        void shouldCreateVideoUploadBuilder() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(100L, null, null, null, null, null)
            );

            Update update = new Update(123L, message, null);
            BotContext context = new BotContext(client, update);

            byte[] file = "video".getBytes();

            // when
            VideoUploadBuilder result =
                    context.video(file, "video.mp4", "video/mp4");

            // then
            assertNotNull(result);
        }
    }

    @Nested
    class DocumentTests {

        @Test
        void shouldCreateDocumentBuilder() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(100L, null, null, null, null, null)
            );

            Update update = new Update(123L, message, null);
            BotContext context = new BotContext(client, update);

            // when
            DocumentBuilder result =
                    context.document("https://example.com/file.pdf");

            // then
            assertNotNull(result);
        }

        @Test
        void shouldCreateDocumentUploadBuilder() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(100L, null, null, null, null, null)
            );

            Update update = new Update(123L, message, null);
            BotContext context = new BotContext(client, update);

            byte[] file = "document".getBytes();

            // when
            DocumentUploadBuilder result =
                    context.document(file, "document.pdf", "application/pdf");

            // then
            assertNotNull(result);
        }
    }

    @Nested
    class ReplyKeyboardTests {

        @Test
        void shouldReturnReplyKeyboardContext() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(100L, null, null, null, null, null)
            );

            Update update = new Update(123L, message, null);
            BotContext context = new BotContext(client, update);

            // when
            ReplyKeyboardContext result = context.replyKeyboard();

            // then
            assertNotNull(result);
        }

        @Test
        void shouldReturnSameReplyKeyboardContextInstance() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(100L, null, null, null, null, null)
            );

            Update update = new Update(123L, message, null);
            BotContext context = new BotContext(client, update);

            // when
            ReplyKeyboardContext first = context.replyKeyboard();
            ReplyKeyboardContext second = context.replyKeyboard();

            // then
            assertSame(first, second);
        }
    }

    @Nested
    class PhotoContextTests {

        @Test
        void shouldReturnPhotoContextWhenMessageExists() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(100L, null, null, null, null, null)
            );

            Update update = new Update(123L, message, null);
            BotContext context = new BotContext(client, update);

            // when
            PhotoContext result = context.photo();

            // then
            assertNotNull(result);
        }

        @Test
        void shouldReturnNullPhotoContextForCallbackQuery() {
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

            Update update = new Update(
                    123L,
                    null,
                    callbackQuery
            );

            BotContext context = new BotContext(client, update);

            // when
            PhotoContext result = context.photo();

            // then
            assertNull(result);
        }
    }

    @Nested
    class CallbackQueryTests {

        @Test
        void shouldReturnCallbackQueryContextWhenCallbackQueryExists() {
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

            Update update = new Update(
                    123L,
                    null,
                    callbackQuery
            );

            BotContext context = new BotContext(client, update);

            // when
            CallbackQueryContext result = context.callbackQuery();

            // then
            assertNotNull(result);
        }

        @Test
        void shouldReturnNullWhenCallbackQueryDoesNotExist() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(100L, null, null, null, null, null)
            );

            Update update = new Update(
                    123L,
                    message,
                    null
            );

            BotContext context = new BotContext(client, update);

            // when
            CallbackQueryContext result = context.callbackQuery();

            // then
            assertNull(result);
        }
    }

    @Nested
    class DeepLinkTests {

        @Test
        void shouldReturnNullDeepLinkParamByDefault() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(100L, null, null, null, null, null)
            );

            Update update = new Update(123L, message, null);
            BotContext context = new BotContext(client, update);

            // when
            String result = context.deepLinkParam();

            // then
            assertNull(result);
        }

        @Test
        void shouldSetAndReturnDeepLinkParam() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(100L, null, null, null, null, null)
            );

            Update update = new Update(123L, message, null);
            BotContext context = new BotContext(client, update);

            // when
            context.setDeepLinkParam("start_123");

            // then
            assertEquals("start_123", context.deepLinkParam());
        }

        @Test
        void shouldReplaceExistingDeepLinkParam() {
            // given
            TelegramClient client = mock(TelegramClient.class);

            Message message = mock(Message.class);
            when(message.chat()).thenReturn(
                    new Chat(100L, null, null, null, null, null)
            );

            Update update = new Update(123L, message, null);
            BotContext context = new BotContext(client, update);

            context.setDeepLinkParam("first");

            // when
            context.setDeepLinkParam("second");

            // then
            assertEquals("second", context.deepLinkParam());
        }
    }
}