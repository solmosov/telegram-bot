package io.github.solmosov.telegrambot.request.message.media;

import io.github.solmosov.telegrambot.model.InputFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SendAudioUploadRequestTest {

    @Test
    @DisplayName("Should correctly build SendAudioUploadRequest using builder")
    void shouldBuildSendAudioUploadRequestSuccessfully(@TempDir Path tempDir) throws IOException, NoSuchFieldException, IllegalAccessException {
        Path tempAudioFile = tempDir.resolve("track.mp3");
        Files.writeString(tempAudioFile, "dummy audio content");

        InputFile audioFile = new InputFile(tempAudioFile, "track.mp3");

        SendAudioUploadRequest request = SendAudioUploadRequest.builder()
                .audio(audioFile)
                .duration(180)
                .build();

        assertNotNull(request, "SendAudioUploadRequest should not be null");

        Field audioField = SendAudioUploadRequest.class.getDeclaredField("audio");
        audioField.setAccessible(true);
        InputFile actualAudio = (InputFile) audioField.get(request);

        Field durationField = SendAudioUploadRequest.class.getDeclaredField("duration");
        durationField.setAccessible(true);
        Integer actualDuration = (Integer) durationField.get(request);

        assertEquals(audioFile, actualAudio, "Audio InputFile reference does not match");
        assertEquals(180, actualDuration, "Duration value does not match");
    }
}