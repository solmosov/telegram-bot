package io.github.shahbozolmosov.telegrambot.client;

import io.github.shahbozolmosov.telegrambot.exception.api.TelegramApiException;
import io.github.shahbozolmosov.telegrambot.exception.client.TelegramClientException;
import io.github.shahbozolmosov.telegrambot.json.ObjectMapperFactory;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.model.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
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
    void setUp() {
        jsonMapper = ObjectMapperFactory.create();

        telegramClient = new TelegramClient(
                "test-token",
                jsonMapper,
                httpClient
        );
    }

    @Nested
    @DisplayName("getUpdates")
    class GetUpdates {
        @Test
        void shouldReturnUpdates() throws IOException, InterruptedException {
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

            mockResponse(json);

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

        @Test
        void shouldSendCorrectRequest() throws IOException, InterruptedException {
            String json = """
                    {
                        "ok": true,
                        "result": []
                    }
                    """;

            mockResponse(json);

            telegramClient.getUpdates(123);

            var captor = ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest request = captor.getValue();

            assertEquals(
                    "https://api.telegram.org/bottest-token/getUpdates?offset=123&timeout=30",
                    request.uri().toString()
            );

            assertEquals("GET", request.method());
        }

        @Test
        void shouldRejectMoreThan100Updates() throws Exception {
            StringBuilder updates = new StringBuilder("[");

            for (int i = 0; i < 101; i++) {
                if (i > 0) {
                    updates.append(",");
                }

                updates.append("""
                        {
                          "update_id": %d
                        }
                        """.formatted(i));
            }

            updates.append("]");

            String json = """
                    {
                      "ok": true,
                      "result": %s
                    }
                    """.formatted(updates);

            mockResponse(json);

            assertThrows(
                    TelegramClientException.class,
                    () -> telegramClient.getUpdates(0)
            );
        }

        @Test
        void shouldReturnEmptyList_whenNoUpdatesAvailable()
                throws Exception {

            String json = """
                    {
                      "ok": true,
                      "result": []
                    }
                    """;

            mockResponse(json);

            TelegramResponse<List<Update>> response =
                    telegramClient.getUpdates(0);

            assertTrue(response.ok());
            assertNotNull(response.result());
            assertTrue(response.result().isEmpty());
        }


        @Test
        void shouldThrowTelegramClientException_whenHttpClientFails()
                throws Exception {

            when(httpClient.send(
                    any(),
                    any(HttpResponse.BodyHandler.class)
            )).thenThrow(new IOException("Connection failed"));

            assertThrows(
                    TelegramClientException.class,
                    () -> telegramClient.getUpdates(0)
            );
        }

        @Test
        void shouldThrowTelegramApiException_whenTelegramReturnsError()
                throws Exception {

            String json = """
                    {
                      "ok": false,
                      "error_code": 400,
                      "description": "Bad Request"
                    }
                    """;

            mockResponse(json);

            TelegramApiException exception = assertThrows(
                    TelegramApiException.class,
                    () -> telegramClient.getUpdates(0)
            );

            assertEquals(400, exception.getErrorCode());
            assertEquals("Bad Request", exception.getMessage());
        }

        @Test
        void shouldThrowTelegramClientException_whenResponseIsInvalidJson()
                throws Exception {

            String invalidJson = """
                    {
                      "ok": true,
                      "result":
                    """;

            mockResponse(invalidJson);

            TelegramClientException exception = assertThrows(
                    TelegramClientException.class,
                    () -> telegramClient.getUpdates(0)
            );

            assertEquals(
                    "Failed to parse Telegram API response.",
                    exception.getMessage()
            );

            assertInstanceOf(
                    JacksonException.class,
                    exception.getCause()
            );
        }
    }


    private void mockResponse(String json) throws IOException, InterruptedException {
        when(httpResponse.body()).thenReturn(json.getBytes(StandardCharsets.UTF_8));

        when(httpClient.send(
                any(),
                any(HttpResponse.BodyHandler.class)
        )).thenReturn(httpResponse);
    }
}