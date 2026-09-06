package io.github.solmosov.telegrambot.messaging;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.solmosov.telegrambot.messaging.builder.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

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

    @Test
    void shouldCreatePhotoUploadBuilder(@TempDir Path tempDir) {
        // given
        Path filePath = tempDir.resolve("photo.jpg");
        String fileName = "photo.jpg";

        // when
        PhotoUploadBuilder result = messaging.photo(filePath, fileName);

        // then
        assertNotNull(result, "PhotoUploadBuilder instance should not be null");
    }

    @Test
    void video_shouldReturnVideoBuilder() {
        assertInstanceOf(VideoBuilder.class, messaging.video("https://example.com/video.mp4"));
    }

    @Test
    void shouldCreateVideoUploadBuilder(@TempDir Path tempDir) {
        // given
        Path filePath = tempDir.resolve("video.mp4");
        String fileName = "video.mp4";

        // when
        VideoUploadBuilder result = messaging.video(filePath, fileName);

        // then
        assertNotNull(result, "VideoUploadBuilder instance should not be null");
    }

    @Test
    void audio_shouldReturnAudioBuilder() {
        assertInstanceOf(
                AudioBuilder.class,
                messaging.audio("https://example.com/audio.mp3")
        );
    }

    @Test
    void shouldCreateAudioUploadBuilder(@TempDir Path tempDir) {
        // given
        Path filePath = tempDir.resolve("audio.mp3");
        String fileName = "audio.mp3";

        // when
        AudioUploadBuilder result = messaging.audio(filePath, fileName);

        // then
        assertNotNull(result, "AudioUploadBuilder instance should not be null");
    }

    @Test
    void document_shouldReturnDocumentBuilder() {
        assertInstanceOf(DocumentBuilder.class, messaging.document("https://example.com/document.pdf"));
    }

    @Test
    void shouldCreateDocumentUploadBuilder(@TempDir Path tempDir) {
        // given
        Path filePath = tempDir.resolve("document.pdf");
        String fileName = "document.pdf";

        // when
        DocumentUploadBuilder result = messaging.document(filePath, fileName);

        // then
        assertNotNull(result, "DocumentUploadBuilder instance should not be null");
    }

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