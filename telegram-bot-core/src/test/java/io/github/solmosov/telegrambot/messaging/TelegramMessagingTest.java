package io.github.solmosov.telegrambot.messaging;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.solmosov.telegrambot.messaging.builder.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TelegramMessagingTest {

    private TelegramMessaging messaging;

    @BeforeEach
    void setUp() {
        TelegramClient client = mock(TelegramClient.class);
        this.messaging = new TelegramMessaging(client);
    }

    @Test
    void chatAction_shouldReturnChatActionBuilder() {
        assertInstanceOf(ChatActionBuilder.class, messaging.chatAction());
    }

    @Test
    void message_shouldReturnMessageBuilder() {
        assertInstanceOf(MessageBuilder.class, messaging.message("Hello"));
    }

    @Test
    void editMessage_shouldReturnEditMessageTextBuilder() {
        assertInstanceOf(EditMessageTextBuilder.class, messaging.editMessage(123L, "Updated"));
    }

    @Test
    void editMessageCaption_shouldReturnEditMessageCaptionBuilder() {
        assertInstanceOf(EditMessageCaptionBuilder.class, messaging.editMessageCaption(123L, "Updated caption"));
    }

    @Test
    void shouldCreateEditInlineKeyboardBuilder() {
        // given
        ReplyMarkup replyMarkup = mock(ReplyMarkup.class);

        // when
        EditMessageReplyMarkupBuilder result =
                messaging.editInlineKeyboard(456L, replyMarkup);

        // then
        assertNotNull(result);
    }

    @Test
    void shouldCreateRemoveInlineKeyboardBuilder() {
        // when
        EditMessageReplyMarkupBuilder result =
                messaging.removeInlineKeyboard(456L);

        // then
        assertNotNull(result);
    }

    @Test
    void deleteMessage_shouldReturnDeleteMessageBuilder() {
        assertInstanceOf(DeleteMessageBuilder.class, messaging.deleteMessage(123L));
    }

    @Test
    void photo_shouldReturnPhotoBuilder() {
        assertInstanceOf(PhotoBuilder.class, messaging.photo("https://example.com/photo.jpg"));
    }

//    @Test
//    void photoUpload_shouldReturnPhotoUploadBuilder() {
//        assertInstanceOf(PhotoUploadBuilder.class, messaging.photo(new byte[]{1, 2, 3}, "photo.jpg", "image/jpeg"));
//    }

    @Test
    void video_shouldReturnVideoBuilder() {
        assertInstanceOf(VideoBuilder.class, messaging.video("https://example.com/video.mp4"));
    }

//    @Test
//    void videoUpload_shouldReturnVideoUploadBuilder() {
//        assertInstanceOf(VideoUploadBuilder.class, messaging.video(new byte[]{1, 2, 3}, "video.mp4", "video/mp4"));
//    }

    @Test
    void audio_shouldReturnAudioBuilder() {
        assertInstanceOf(
                AudioBuilder.class,
                messaging.audio("https://example.com/audio.mp3")
        );
    }

//    @Test
//    void audioUpload_shouldReturnAudioUploadBuilder() {
//        assertInstanceOf(
//                AudioUploadBuilder.class,
//                messaging.audio(new byte[]{1, 2, 3}, "audio.mp3", "audio/mpeg")
//        );
//    }

    @Test
    void document_shouldReturnDocumentBuilder() {
        assertInstanceOf(DocumentBuilder.class, messaging.document("https://example.com/document.pdf"));
    }

//    @Test
//    void documentUpload_shouldReturnDocumentUploadBuilder() {
//        assertInstanceOf(DocumentUploadBuilder.class, messaging.document(new byte[]{1, 2, 3}, "document.pdf", "application/pdf"));
//    }

    @Test
    void allBuilders_shouldNotBeNull() {
        assertNotNull(messaging.chatAction());
        assertNotNull(messaging.message("Hello"));
        assertNotNull(messaging.editMessage(123L, "Updated"));
        assertNotNull(messaging.editMessageCaption(123L, "Caption"));
        assertNotNull(messaging.deleteMessage(123L));
        assertNotNull(messaging.photo("photo.jpg"));
//        assertNotNull(messaging.photo(new byte[]{1}, "photo.jpg", "image/jpeg"));
        assertNotNull(messaging.video("video.mp4"));
//        assertNotNull(messaging.video(new byte[]{1}, "video.mp4", "video/mp4"));
        assertNotNull(messaging.audio("audio.mp3"));
//        assertNotNull(messaging.audio(new byte[]{1}, "audio.mp3", "audio/mpeg"));
        assertNotNull(messaging.document("document.pdf"));
//        assertNotNull(messaging.document(new byte[]{1}, "document.pdf", "application/pdf"));
    }

}