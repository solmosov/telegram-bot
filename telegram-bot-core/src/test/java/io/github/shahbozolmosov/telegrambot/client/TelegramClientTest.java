package io.github.shahbozolmosov.telegrambot.client;

import io.github.shahbozolmosov.telegrambot.json.ObjectMapperFactory;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.model.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TelegramClient Tests")
class TelegramClientTest {
    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<byte[]> httpResponse;

    private TelegramClient telegramClient;

    private JsonMapper jsonMapper;

    @BeforeEach
    void setUp(){
        jsonMapper = ObjectMapperFactory.create();

        telegramClient = new TelegramClient(
                "test-token",
                jsonMapper,
                httpClient
        );
    }

    @Test
    void getUpdates_shouldReturnUpdates() throws IOException, InterruptedException {
        String json = """
                {
                  "ok": true,
                  "result": [
                    {
                      "update_id": 123
                    }
                  ]
                }
                """;

        when(httpResponse.body()).thenReturn(json.getBytes(StandardCharsets.UTF_8));

        when(httpClient.send(
                any(),
                any(HttpResponse.BodyHandler.class)
        )).thenReturn(httpResponse);

        TelegramResponse<List<Update>> response = telegramClient.getUpdates(100);

        assertNotNull(response);
        assertTrue(response.ok());
        assertNotNull(response.result());
        assertEquals(1, response.result().size());
        assertEquals(123, response.result().getFirst().updateId());

        verify(httpClient).send(
                any(),
                any(HttpResponse.BodyHandler.class)
        );
    }
}