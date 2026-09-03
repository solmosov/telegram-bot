package io.github.solmosov.telegrambot.context;

import io.github.solmosov.telegrambot.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageContextTest {

    private Message message;
    private MessageContext context;

    @BeforeEach
    void setUp() {
        message = mock(Message.class);
        context = new MessageContext(message);
    }

    @Test
    void shouldReturnMessage() {
        assertSame(message, context.message());
    }

    @Test
    void shouldReturnMessageId() {
        when(message.messageId()).thenReturn(123L);

        assertEquals(123L, context.messageId());
    }

    @Test
    void shouldReturnChatId() {
        Chat chat = mock(Chat.class);

        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(456L);

        assertEquals(456L, context.chatId());
    }

    @Test
    void shouldReturnFrom() {
        From from = new From(
                1L,
                false,
                "John",
                "Doe",
                "en"
        );

        when(message.from()).thenReturn(from);

        assertSame(from, context.from());
    }

    @Test
    void shouldReturnDate() {
        when(message.date()).thenReturn(123456789L);

        assertEquals(123456789L, context.date());
    }

    @Test
    void shouldReturnText() {
        when(message.text()).thenReturn("Hello");

        assertEquals("Hello", context.text());
    }

    @Test
    void shouldReturnPhoto() {
        List<PhotoSize> photo = List.of(mock(PhotoSize.class));

        when(message.photo()).thenReturn(photo);

        assertSame(photo, context.photo());
    }

    @Test
    void shouldReturnDocument() {
        DocumentInfo document = mock(DocumentInfo.class);

        when(message.document()).thenReturn(document);

        assertSame(document, context.document());
    }

    @Test
    void shouldReturnCaption() {
        when(message.caption()).thenReturn("Caption");

        assertEquals("Caption", context.caption());
    }

    @Test
    void shouldReturnReplyToMessage() {
        ReplyToMessage replyToMessage = mock(ReplyToMessage.class);

        when(message.replyToMessage()).thenReturn(replyToMessage);

        assertSame(replyToMessage, context.replyToMessage());
    }

    @Test
    void shouldReturnLocation() {
        Location location = mock(Location.class);

        when(message.location()).thenReturn(location);

        assertSame(location, context.location());
    }

    @Test
    void shouldReturnContact() {
        Contact contact = mock(Contact.class);

        when(message.contact()).thenReturn(contact);

        assertSame(contact, context.contact());
    }

    @Test
    void shouldReturnUsersShared() {
        UsersShared usersShared = mock(UsersShared.class);

        when(message.usersShared()).thenReturn(usersShared);

        assertSame(usersShared, context.usersShared());
    }
}