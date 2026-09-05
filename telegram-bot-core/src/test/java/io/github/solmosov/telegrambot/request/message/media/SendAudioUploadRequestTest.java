package io.github.solmosov.telegrambot.request.message.media;

import io.github.solmosov.telegrambot.json.ObjectMapperFactory;
import io.github.solmosov.telegrambot.model.InputFile;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

class SendAudioUploadRequestTest {

    private final ObjectMapper objectMapper = ObjectMapperFactory.create();

//    @Test
//    void shouldBuildRequestWithAudioAndDuration() {
//        byte[] data = {1, 2, 3};
//        InputFile audio = new InputFile(data, "audio.mp3", "audio/mpeg");
//
//        SendAudioUploadRequest request = SendAudioUploadRequest.builder()
//                .audio(audio)
//                .duration(120)
//                .build();
//
//        assertNotNull(request);
//    }
//
//    @Test
//    void shouldSerializeAudioAsInputFile() throws Exception {
//        InputFile audio = new InputFile(
//                new byte[]{1, 2, 3},
//                "audio.mp3",
//                "audio/mpeg"
//        );
//
//        SendAudioUploadRequest request = SendAudioUploadRequest.builder()
//                .audio(audio)
//                .duration(120)
//                .build();
//
//        JsonNode json = objectMapper.readTree(
//                objectMapper.writeValueAsString(request)
//        );
//
//        assertEquals(120, json.get("duration").asInt());
//        assertTrue(json.has("audio"));
//    }

    @Test
    void shouldOmitNullOptionalFields() throws Exception {
        SendAudioUploadRequest request = SendAudioUploadRequest.builder()
                .duration(120)
                .build();

        JsonNode json = objectMapper.readTree(
                objectMapper.writeValueAsString(request)
        );

        assertEquals(120, json.get("duration").asInt());
        assertFalse(json.has("audio"));
        assertFalse(json.has("caption"));
        assertFalse(json.has("parse_mode"));
        assertFalse(json.has("reply_markup"));
    }

//    @Test
//    void shouldPreserveInputFileData() {
//        byte[] data = {1, 2, 3};
//        InputFile audio = new InputFile(data, "audio.mp3", "audio/mpeg");
//
//        assertArrayEquals(data, audio.getData());
//        assertEquals("audio.mp3", audio.getFileName());
//        assertEquals("audio/mpeg", audio.getMimeType());
//    }
//
//    @Test
//    void shouldSupportMediaRequestFields() throws Exception {
//        InputFile audio = new InputFile(
//                new byte[]{1, 2, 3},
//                "audio.mp3",
//                "audio/mpeg"
//        );
//
//        SendAudioUploadRequest request = SendAudioUploadRequest.builder()
//                .audio(audio)
//                .duration(120)
//                .caption("Test audio")
//                .html()
//                .build();
//
//        JsonNode json = objectMapper.readTree(
//                objectMapper.writeValueAsString(request)
//        );
//
//        assertEquals(120, json.get("duration").asInt());
//        assertEquals("Test audio", json.get("caption").asString());
//        assertEquals("HTML", json.get("parse_mode").asString());
//        assertTrue(json.has("audio"));
//    }
}