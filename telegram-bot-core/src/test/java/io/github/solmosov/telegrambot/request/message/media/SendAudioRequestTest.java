package io.github.solmosov.telegrambot.request.message.media;

import io.github.solmosov.telegrambot.json.ObjectMapperFactory;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SendAudioRequestTest {

    private final ObjectMapper objectMapper = ObjectMapperFactory.create();

    @Test
    void shouldSerializeAudioUrl() throws Exception {
        String audioUrl = "https://example.com/audio.mp3";

        SendAudioRequest request = SendAudioRequest.builder()
                .audio(audioUrl)
                .duration(120)
                .build();

        JsonNode json = objectMapper.readTree(
                objectMapper.writeValueAsString(request)
        );

        assertEquals(audioUrl, json.get("audio").asString());
        assertEquals(120, json.get("duration").asInt());
    }
}