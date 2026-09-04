package io.github.solmosov.telegrambot.client;

import io.github.solmosov.telegrambot.exception.api.TelegramApiException;
import io.github.solmosov.telegrambot.exception.client.TelegramClientException;
import io.github.solmosov.telegrambot.json.ObjectMapperFactory;
import io.github.solmosov.telegrambot.model.*;
import io.github.solmosov.telegrambot.request.callback.AnswerCallbackRequest;
import io.github.solmosov.telegrambot.request.message.media.*;
import io.github.solmosov.telegrambot.request.message.media.*;
import io.github.solmosov.telegrambot.request.message.message_action.DeleteMessageRequest;
import io.github.solmosov.telegrambot.request.message.text.EditMessageTextRequest;
import io.github.solmosov.telegrambot.request.message.text.SendMessageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Flow;

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
    void setUp() throws Exception {
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

    @Nested
    @DisplayName("Webhook Tests")
    class WebhookTests {

        @Test
        @DisplayName("should set webhook")
        void shouldSetWebhook() throws Exception {
            // Given
            mockHttpResponse("""
            {
              "ok": true,
              "result": true
            }
            """);

            String webhookUrl = "https://example.com/telegram/webhook";
            String secret = "my-secret_123";

            // When
            TelegramResponse<Boolean> response =
                    telegramClient.setWebhook(webhookUrl, secret);

            // Then
            assertNotNull(response);
            assertTrue(response.ok());
            assertTrue(response.result());

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest request = captor.getValue();

            assertEquals("POST", request.method());
            assertEquals(
                    "https://api.telegram.org/bottest-token/setWebhook"
                            + "?url=https%3A%2F%2Fexample.com%2Ftelegram%2Fwebhook"
                            + "&secret_token=my-secret_123",
                    request.uri().toString()
            );
        }

        @Test
        @DisplayName("should get webhook info")
        void shouldGetWebhookInfo() throws Exception {
            // Given
            mockHttpResponse("""
            {
              "ok": true,
              "result": {
                "url": "https://example.com/webhook",
                "has_custom_certificate": false,
                "pending_update_count": 0
              }
            }
            """);

            // When
            TelegramResponse<WebhookInfo> response =
                    telegramClient.getWebhookInfo();

            // Then
            assertNotNull(response);
            assertTrue(response.ok());
            assertNotNull(response.result());

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest request = captor.getValue();

            assertEquals("GET", request.method());
            assertEquals(
                    "https://api.telegram.org/bottest-token/getWebhookInfo",
                    request.uri().toString()
            );
        }

        @Test
        @DisplayName("should delete webhook")
        void shouldDeleteWebhook() throws Exception {
            // Given
            mockHttpResponse("""
            {
              "ok": true,
              "result": true
            }
            """);

            // When
            TelegramResponse<Boolean> response =
                    telegramClient.deleteWebhook();

            // Then
            assertNotNull(response);
            assertTrue(response.ok());
            assertTrue(response.result());

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest request = captor.getValue();

            assertEquals("POST", request.method());
            assertEquals(
                    "https://api.telegram.org/bottest-token/deleteWebhook",
                    request.uri().toString()
            );
        }
    }

    @Nested
    @DisplayName("sendMessage")
    class SendMessage {

        @Test
        void shouldReturnMessage() throws Exception {
            String json = """
                        {
                            "ok": true,
                            "result": {
                                "message_id": 123,
                                "chat": {
                                  "id": 456
                                },
                                "text": "Hello"
                            }
                        }
                    """;

            mockResponse(json);

            SendMessageRequest request = SendMessageRequest.builder()
                    .chatId(456)
                    .text("Hello")
                    .build();

            TelegramResponse<Message> response = telegramClient.sendMessage(request);

            assertNotNull(response);
            assertTrue(response.ok());
            assertNotNull(response.result());
            assertEquals(123, response.result().messageId());
            assertEquals("Hello", response.result().text());
        }

        @Test
        void shouldSendCorrectRequest() throws Exception {
            mockResponse("""
                    {
                      "ok": true,
                      "result": {
                        "message_id": 123,
                        "chat": {
                          "id": 456
                        },
                        "text": "Hello"
                      }
                    }
                    """);

            SendMessageRequest request = SendMessageRequest.builder()
                    .chatId(456)
                    .text("Hello")
                    .build();

            telegramClient.sendMessage(request);

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest httpRequest = captor.getValue();

            assertEquals(
                    "https://api.telegram.org/bottest-token/sendMessage",
                    httpRequest.uri().toString()
            );

            assertEquals("POST", httpRequest.method());

            assertEquals(
                    "application/json",
                    httpRequest.headers()
                            .firstValue("Content-Type")
                            .orElseThrow()
            );

            assertTrue(httpRequest.bodyPublisher().isPresent());

            String body = new String(
                    httpRequest.bodyPublisher()
                            .orElseThrow()
                            .contentLength() > 0
                            ? readBody(httpRequest)
                            : new byte[0],
                    StandardCharsets.UTF_8
            );

            assertEquals(
                    """
                            {"chat_id":456,"text":"Hello"}""",
                    body
            );
        }

        @Test
        void shouldThrowTelegramApiException_whenTelegramReturnsError()
                throws Exception {

            mockResponse("""
                    {
                      "ok": false,
                      "error_code": 400,
                      "description": "Bad Request: chat not found"
                    }
                    """);

            SendMessageRequest request = SendMessageRequest.builder()
                    .chatId(456L)
                    .text("Hello")
                    .build();

            TelegramApiException exception = assertThrows(
                    TelegramApiException.class,
                    () -> telegramClient.sendMessage(request)
            );

            assertEquals(400, exception.getErrorCode());
            assertEquals(
                    "Bad Request: chat not found",
                    exception.getMessage()
            );
        }

        @Test
        void shouldThrowTelegramClientException_whenHttpClientFails()
                throws Exception {

            when(httpClient.send(
                    any(),
                    any(HttpResponse.BodyHandler.class)
            )).thenThrow(new IOException("Connection failed"));

            SendMessageRequest request = SendMessageRequest.builder()
                    .chatId(456L)
                    .text("Hello")
                    .build();

            TelegramClientException exception = assertThrows(
                    TelegramClientException.class,
                    () -> telegramClient.sendMessage(request)
            );

            assertEquals(
                    "Failed to communicate with Telegram API",
                    exception.getMessage()
            );

            assertInstanceOf(
                    IOException.class,
                    exception.getCause()
            );
        }

    }


    @Nested
    @DisplayName("editMessage")
    class EditMessageText {

        @Test
        void shouldReturnMessage() throws Exception {
            mockResponse("""
                    {
                      "ok": true,
                      "result": {
                        "message_id": 123,
                        "chat": {
                          "id": 456
                        },
                        "text": "Edited message"
                      }
                    }
                    """);

            EditMessageTextRequest request = EditMessageTextRequest.builder()
                    .chatId(456L)
                    .messageId(123L)
                    .text("Edited message")
                    .build();

            TelegramResponse<Message> response =
                    telegramClient.editMessage(request);

            assertNotNull(response);
            assertTrue(response.ok());
            assertNotNull(response.result());
            assertEquals(123, response.result().messageId());
            assertEquals("Edited message", response.result().text());
        }

        @Test
        void shouldSendCorrectRequest() throws Exception {
            mockResponse("""
                    {
                      "ok": true,
                      "result": {
                        "message_id": 123,
                        "chat": {
                          "id": 456
                        },
                        "text": "Edited message"
                      }
                    }
                    """);

            EditMessageTextRequest request = EditMessageTextRequest.builder()
                    .chatId(456L)
                    .messageId(123L)
                    .text("Edited message")
                    .build();

            telegramClient.editMessage(request);

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest httpRequest = captor.getValue();

            assertEquals(
                    "https://api.telegram.org/bottest-token/editMessageText",
                    httpRequest.uri().toString()
            );

            assertEquals("POST", httpRequest.method());

            assertEquals(
                    "application/json",
                    httpRequest.headers()
                            .firstValue("Content-Type")
                            .orElseThrow()
            );
        }

        @Test
        void shouldThrowTelegramApiException_whenTelegramReturnsError()
                throws Exception {

            mockResponse("""
                    {
                      "ok": false,
                      "error_code": 400,
                      "description": "Bad Request: message to edit not found"
                    }
                    """);

            EditMessageTextRequest request = EditMessageTextRequest.builder()
                    .chatId(456L)
                    .messageId(123L)
                    .text("Edited message")
                    .build();

            TelegramApiException exception = assertThrows(
                    TelegramApiException.class,
                    () -> telegramClient.editMessage(request)
            );

            assertEquals(400, exception.getErrorCode());
            assertEquals(
                    "Bad Request: message to edit not found",
                    exception.getMessage()
            );
        }

        @Test
        void shouldThrowTelegramClientException_whenHttpClientFails()
                throws Exception {

            when(httpClient.send(
                    any(),
                    any(HttpResponse.BodyHandler.class)
            )).thenThrow(new IOException("Connection failed"));

            EditMessageTextRequest request = EditMessageTextRequest.builder()
                    .chatId(456L)
                    .messageId(123L)
                    .text("Edited message")
                    .build();

            TelegramClientException exception = assertThrows(
                    TelegramClientException.class,
                    () -> telegramClient.editMessage(request)
            );

            assertEquals(
                    "Failed to communicate with Telegram API",
                    exception.getMessage()
            );

            assertInstanceOf(
                    IOException.class,
                    exception.getCause()
            );
        }
    }

    @Nested
    @DisplayName("editMessageCaption")
    class EditMessageCaption {

        @Test
        void shouldReturnMessage() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": {
                    "message_id": 123,
                    "chat": {
                      "id": 456
                    },
                    "caption": "Edited caption"
                  }
                }
                """);

            EditMessageCaptionRequest request =
                    EditMessageCaptionRequest.builder()
                            .chatId(456L)
                            .messageId(123L)
                            .caption("Edited caption")
                            .build();

            TelegramResponse<Message> response =
                    telegramClient.editMessageCaption(request);

            assertNotNull(response);
            assertTrue(response.ok());
            assertNotNull(response.result());
            assertEquals(123, response.result().messageId());
        }

        @Test
        void shouldSendCorrectRequest() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": {
                    "message_id": 123,
                    "chat": {
                      "id": 456
                    },
                    "caption": "Edited caption"
                  }
                }
                """);

            EditMessageCaptionRequest request =
                    EditMessageCaptionRequest.builder()
                            .chatId(456L)
                            .messageId(123L)
                            .caption("Edited caption")
                            .build();

            telegramClient.editMessageCaption(request);

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest httpRequest = captor.getValue();

            assertEquals(
                    "https://api.telegram.org/bottest-token/editMessageCaption",
                    httpRequest.uri().toString()
            );

            assertEquals("POST", httpRequest.method());

            assertEquals(
                    "application/json",
                    httpRequest.headers()
                            .firstValue("Content-Type")
                            .orElseThrow()
            );
        }

        @Test
        void shouldThrowTelegramApiException_whenTelegramReturnsError()
                throws Exception {

            mockResponse("""
                {
                  "ok": false,
                  "error_code": 400,
                  "description": "Bad Request"
                }
                """);

            EditMessageCaptionRequest request =
                    EditMessageCaptionRequest.builder()
                            .chatId(456L)
                            .messageId(123L)
                            .caption("Edited caption")
                            .build();

            TelegramApiException exception = assertThrows(
                    TelegramApiException.class,
                    () -> telegramClient.editMessageCaption(request)
            );

            assertEquals(400, exception.getErrorCode());
            assertEquals("Bad Request", exception.getMessage());
        }

        @Test
        void shouldThrowTelegramClientException_whenHttpClientFails()
                throws Exception {

            when(httpClient.send(
                    any(),
                    any(HttpResponse.BodyHandler.class)
            )).thenThrow(new IOException("Connection failed"));

            EditMessageCaptionRequest request =
                    EditMessageCaptionRequest.builder()
                            .chatId(456L)
                            .messageId(123L)
                            .caption("Edited caption")
                            .build();

            TelegramClientException exception = assertThrows(
                    TelegramClientException.class,
                    () -> telegramClient.editMessageCaption(request)
            );

            assertEquals(
                    "Failed to communicate with Telegram API",
                    exception.getMessage()
            );

            assertInstanceOf(
                    IOException.class,
                    exception.getCause()
            );
        }
    }


    @Nested
    @DisplayName("deleteMessage")
    class DeleteMessage {

        @Test
        void shouldReturnTrue() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": true
                }
                """);

            DeleteMessageRequest request =
                    new DeleteMessageRequest(456L, 123L);

            TelegramResponse<Boolean> response =
                    telegramClient.deleteMessage(request);

            assertNotNull(response);
            assertTrue(response.ok());
            assertNotNull(response.result());
            assertTrue(response.result());
        }

        @Test
        void shouldSendCorrectRequest() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": true
                }
                """);

            DeleteMessageRequest request =
                    new DeleteMessageRequest(456L, 123L);

            telegramClient.deleteMessage(request);

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest httpRequest = captor.getValue();

            assertEquals(
                    "https://api.telegram.org/bottest-token/deleteMessage",
                    httpRequest.uri().toString()
            );

            assertEquals("POST", httpRequest.method());

            assertEquals(
                    "application/json",
                    httpRequest.headers()
                            .firstValue("Content-Type")
                            .orElseThrow()
            );
        }
    }

    @Nested
    @DisplayName("answerCallbackQuery")
    class AnswerCallbackQuery {

        @Test
        void shouldReturnTrue() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": true
                }
                """);

            AnswerCallbackRequest request =
                    new AnswerCallbackRequest(
                            "callback-123",
                            "Done!",
                            true
                    );

            TelegramResponse<Boolean> response =
                    telegramClient.answerCallbackQuery(request);

            assertNotNull(response);
            assertTrue(response.ok());
            assertTrue(response.result());
        }

        @Test
        void shouldSendCorrectRequest() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": true
                }
                """);

            AnswerCallbackRequest request =
                    new AnswerCallbackRequest(
                            "callback-123",
                            "Done!",
                            true
                    );

            telegramClient.answerCallbackQuery(request);

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest httpRequest = captor.getValue();

            assertEquals(
                    "https://api.telegram.org/bottest-token/answerCallbackQuery",
                    httpRequest.uri().toString()
            );

            assertEquals("POST", httpRequest.method());

            assertEquals(
                    "application/json",
                    httpRequest.headers()
                            .firstValue("Content-Type")
                            .orElseThrow()
            );
        }

        @Test
        void shouldOmitNullFields() throws Exception {
            mockResponse("""
            {
              "ok": true,
              "result": true
            }
            """);

            AnswerCallbackRequest request =
                    new AnswerCallbackRequest(
                            "callback-123",
                            null,
                            null
                    );

            telegramClient.answerCallbackQuery(request);

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            String body = readRequestBody(captor.getValue());

            assertEquals(
                    "{\"callback_query_id\":\"callback-123\"}",
                    body
            );
        }
    }

    @Nested
    @DisplayName("sendDocument")
    class SendDocument {

        @Test
        void shouldReturnMessage() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": {
                    "message_id": 123,
                    "chat": {
                      "id": 456
                    }
                  }
                }
                """);

            SendDocumentRequest request = SendDocumentRequest.builder()
                    .chatId(456L)
                    .document("document-id")
                    .build();

            TelegramResponse<Message> response =
                    telegramClient.sendDocument(request);

            assertNotNull(response);
            assertTrue(response.ok());
            assertNotNull(response.result());
            assertEquals(123, response.result().messageId());
        }

        @Test
        void shouldSendCorrectRequest() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": {
                    "message_id": 123,
                    "chat": {
                      "id": 456
                    }
                  }
                }
                """);

            SendDocumentRequest request = SendDocumentRequest.builder()
                    .chatId(456L)
                    .document("document-id")
                    .build();

            telegramClient.sendDocument(request);

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest httpRequest = captor.getValue();

            assertEquals(
                    "https://api.telegram.org/bottest-token/sendDocument",
                    httpRequest.uri().toString()
            );

            assertEquals("POST", httpRequest.method());

            assertEquals(
                    "application/json",
                    httpRequest.headers()
                            .firstValue("Content-Type")
                            .orElseThrow()
            );

            JsonNode body =
                    jsonMapper.readTree(readRequestBody(httpRequest));

            assertEquals(
                    456,
                    body.get("chat_id").asLong()
            );

            assertEquals(
                    "document-id",
                    body.get("document").asText()
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

    @Nested
    @DisplayName("sendDocument upload")
    class SendDocumentUpload {

        @Test
        void shouldReturnMessage() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": {
                    "message_id": 123,
                    "chat": {
                      "id": 456
                    }
                  }
                }
                """);

            InputFile document = new InputFile(
                    "test document".getBytes(StandardCharsets.UTF_8),
                    "test.txt",
                    "text/plain"
            );

            SendDocumentUploadRequest request =
                    SendDocumentUploadRequest.builder()
                            .chatId(456L)
                            .document(document)
                            .build();

            TelegramResponse<Message> response =
                    telegramClient.sendDocument(request);

            assertNotNull(response);
            assertTrue(response.ok());
            assertNotNull(response.result());
            assertEquals(123, response.result().messageId());
        }

        @Test
        void shouldSendCorrectMultipartRequest() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": {
                    "message_id": 123,
                    "chat": {
                      "id": 456
                    }
                  }
                }
                """);

            byte[] fileData = "test document".getBytes(StandardCharsets.UTF_8);

            InputFile document = new InputFile(
                    fileData,
                    "test.txt",
                    "text/plain"
            );

            SendDocumentUploadRequest request =
                    SendDocumentUploadRequest.builder()
                            .chatId(456L)
                            .document(document)
                            .disableContentTypeDetection()
                            .build();

            telegramClient.sendDocument(request);

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest httpRequest = captor.getValue();

            assertEquals(
                    "https://api.telegram.org/bottest-token/sendDocument",
                    httpRequest.uri().toString()
            );

            assertEquals("POST", httpRequest.method());

            String contentType = httpRequest.headers()
                    .firstValue("Content-Type")
                    .orElseThrow();

            assertTrue(
                    contentType.startsWith("multipart/form-data; boundary=")
            );

            byte[] body = readRequestBodyBytes(httpRequest);

            String bodyString = new String(
                    body,
                    StandardCharsets.UTF_8
            );

            assertTrue(bodyString.contains("name=\"chat_id\""));
            assertTrue(bodyString.contains("456"));

            assertTrue(
                    bodyString.contains(
                            "name=\"disable_content_type_detection\""
                    )
            );
            assertTrue(bodyString.contains("true"));

            assertTrue(bodyString.contains("name=\"document\""));
            assertTrue(bodyString.contains("filename=\"test.txt\""));
            assertTrue(bodyString.contains("Content-Type: text/plain"));

            assertTrue(bodyString.contains("test document"));
        }
    }

    @Nested
    @DisplayName("sendPhoto")
    class SendPhoto {

        @Test
        void shouldReturnMessage() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": {
                    "message_id": 123,
                    "chat": {
                      "id": 456
                    },
                    "caption": "Test photo"
                  }
                }
                """);

            SendPhotoRequest request =
                    SendPhotoRequest.builder()
                            .chatId(456L)
                            .photo("https://example.com/AgACAgIAAxkBAAIB.png")
                            .caption("Test photo")
                            .hasSpoiler()
                            .showCaptionAboveMedia()
                            .build();

            TelegramResponse<Message> response =
                    telegramClient.sendPhoto(request);

            assertNotNull(response);
            assertTrue(response.ok());
            assertNotNull(response.result());
            assertEquals(123, response.result().messageId());
        }

        @Test
        void shouldSendCorrectRequest() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": {
                    "message_id": 123,
                    "chat": {
                      "id": 456
                    }
                  }
                }
                """);

            SendPhotoRequest request =
                    SendPhotoRequest.builder()
                            .chatId(456L)
                            .photo("https://example.com/AgACAgIAAxkBAAIB.png")
                            .caption("Test photo")
                            .hasSpoiler()
                            .showCaptionAboveMedia()
                            .build();

            telegramClient.sendPhoto(request);

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest httpRequest = captor.getValue();

            assertEquals(
                    "https://api.telegram.org/bottest-token/sendPhoto",
                    httpRequest.uri().toString()
            );

            assertEquals("POST", httpRequest.method());

            assertEquals(
                    "application/json",
                    httpRequest.headers()
                            .firstValue("Content-Type")
                            .orElseThrow()
            );

            JsonNode body =
                    jsonMapper.readTree(readRequestBody(httpRequest));

            assertEquals(
                    456,
                    body.get("chat_id").asLong()
            );

            assertEquals(
                    "https://example.com/AgACAgIAAxkBAAIB.png",
                    body.get("photo").asText()
            );

            assertEquals(
                    "Test photo",
                    body.get("caption").asText()
            );

            assertTrue(body.get("has_spoiler").asBoolean());

            assertTrue(
                    body.get("show_caption_above_media").asBoolean()
            );
        }
    }

    @Nested
    @DisplayName("sendPhoto upload")
    class SendPhotoUpload {

        @Test
        void shouldReturnMessage() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": {
                    "message_id": 123,
                    "chat": {
                      "id": 456
                    }
                  }
                }
                """);

            InputFile photo = new InputFile(
                    "fake image data".getBytes(StandardCharsets.UTF_8),
                    "photo.jpg",
                    "image/jpeg"
            );

            SendPhotoUploadRequest request =
                    SendPhotoUploadRequest.builder()
                            .chatId(456L)
                            .photo(photo)
                            .caption("Test photo")
                            .hasSpoiler()
                            .showCaptionAboveMedia()
                            .build();

            TelegramResponse<Message> response =
                    telegramClient.sendPhoto(request);

            assertNotNull(response);
            assertTrue(response.ok());
            assertNotNull(response.result());
            assertEquals(123, response.result().messageId());
        }

        @Test
        void shouldSendCorrectMultipartRequest() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": {
                    "message_id": 123,
                    "chat": {
                      "id": 456
                    }
                  }
                }
                """);

            byte[] photoData =
                    "fake image data".getBytes(StandardCharsets.UTF_8);

            InputFile photo = new InputFile(
                    photoData,
                    "photo.jpg",
                    "image/jpeg"
            );

            SendPhotoUploadRequest request =
                    SendPhotoUploadRequest.builder()
                            .chatId(456L)
                            .photo(photo)
                            .caption("Test photo")
                            .hasSpoiler()
                            .showCaptionAboveMedia()
                            .build();

            telegramClient.sendPhoto(request);

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest httpRequest = captor.getValue();

            assertEquals(
                    "https://api.telegram.org/bottest-token/sendPhoto",
                    httpRequest.uri().toString()
            );

            assertEquals("POST", httpRequest.method());

            String contentType = httpRequest.headers()
                    .firstValue("Content-Type")
                    .orElseThrow();

            assertTrue(
                    contentType.startsWith("multipart/form-data; boundary=")
            );

            byte[] body = readRequestBodyBytes(httpRequest);

            String bodyString = new String(
                    body,
                    StandardCharsets.UTF_8
            );

            assertTrue(bodyString.contains("name=\"chat_id\""));
            assertTrue(bodyString.contains("456"));

            assertTrue(bodyString.contains("name=\"caption\""));
            assertTrue(bodyString.contains("Test photo"));

            assertTrue(
                    bodyString.contains("name=\"has_spoiler\"")
            );
            assertTrue(bodyString.contains("true"));

            assertTrue(
                    bodyString.contains("name=\"show_caption_above_media\"")
            );
            assertTrue(bodyString.contains("true"));

            assertTrue(bodyString.contains("name=\"photo\""));
            assertTrue(bodyString.contains("filename=\"photo.jpg\""));
            assertTrue(bodyString.contains("Content-Type: image/jpeg"));
            assertTrue(bodyString.contains("fake image data"));
        }
    }

    @Nested
    @DisplayName("sendVideo")
    class SendVideo {

        @Test
        void shouldReturnMessage() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": {
                    "message_id": 123,
                    "chat": {
                      "id": 456
                    },
                    "caption": "Test video"
                  }
                }
                """);

            SendVideoRequest request =
                    SendVideoRequest.builder()
                            .chatId(456L)
                            .video("https://example.com/video.mp4")
                            .duration(120)
                            .width(1920)
                            .height(1080)
                            .hasSpoiler()
                            .showCaptionAboveMedia()
                            .caption("Test video")
                            .build();

            TelegramResponse<Message> response =
                    telegramClient.sendVideo(request);

            assertNotNull(response);
            assertTrue(response.ok());
            assertNotNull(response.result());
            assertEquals(123, response.result().messageId());
        }

        @Test
        void shouldSendCorrectRequest() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": {
                    "message_id": 123,
                    "chat": {
                      "id": 456
                    }
                  }
                }
                """);

            SendVideoRequest request =
                    SendVideoRequest.builder()
                            .chatId(456L)
                            .video("https://example.com/video.mp4")
                            .duration(120)
                            .width(1920)
                            .height(1080)
                            .hasSpoiler()
                            .showCaptionAboveMedia()
                            .caption("Test video")
                            .build();

            telegramClient.sendVideo(request);

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest httpRequest = captor.getValue();

            assertEquals(
                    "https://api.telegram.org/bottest-token/sendVideo",
                    httpRequest.uri().toString()
            );

            assertEquals("POST", httpRequest.method());

            assertEquals(
                    "application/json",
                    httpRequest.headers()
                            .firstValue("Content-Type")
                            .orElseThrow()
            );

            JsonNode body =
                    jsonMapper.readTree(readRequestBody(httpRequest));

            assertEquals(456, body.get("chat_id").asLong());
            assertEquals(
                    "https://example.com/video.mp4",
                    body.get("video").asText()
            );
            assertEquals(120, body.get("duration").asInt());
            assertEquals(1920, body.get("width").asInt());
            assertEquals(1080, body.get("height").asInt());
            assertTrue(body.get("has_spoiler").asBoolean());
            assertTrue(
                    body.get("show_caption_above_media").asBoolean()
            );
            assertEquals("Test video", body.get("caption").asText());
        }
    }

    @Nested
    @DisplayName("sendVideo upload")
    class SendVideoUpload {

        @Test
        void shouldReturnMessage() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": {
                    "message_id": 123,
                    "chat": {
                      "id": 456
                    }
                  }
                }
                """);

            InputFile video = new InputFile(
                    "fake video data".getBytes(StandardCharsets.UTF_8),
                    "video.mp4",
                    "video/mp4"
            );

            SendVideoUploadRequest request =
                    SendVideoUploadRequest.builder()
                            .chatId(456L)
                            .video(video)
                            .duration(120)
                            .width(1920)
                            .height(1080)
                            .hasSpoiler()
                            .showCaptionAboveMedia()
                            .caption("Test video")
                            .build();

            TelegramResponse<Message> response =
                    telegramClient.sendVideo(request);

            assertNotNull(response);
            assertTrue(response.ok());
            assertNotNull(response.result());
            assertEquals(123, response.result().messageId());
        }

        @Test
        void shouldSendCorrectMultipartRequest() throws Exception {
            mockResponse("""
                {
                  "ok": true,
                  "result": {
                    "message_id": 123,
                    "chat": {
                      "id": 456
                    }
                  }
                }
                """);

            byte[] videoData =
                    "fake video data".getBytes(StandardCharsets.UTF_8);

            InputFile video = new InputFile(
                    videoData,
                    "video.mp4",
                    "video/mp4"
            );

            SendVideoUploadRequest request =
                    SendVideoUploadRequest.builder()
                            .chatId(456L)
                            .video(video)
                            .duration(120)
                            .width(1920)
                            .height(1080)
                            .hasSpoiler()
                            .showCaptionAboveMedia()
                            .caption("Test video")
                            .build();

            telegramClient.sendVideo(request);

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest httpRequest = captor.getValue();

            assertEquals(
                    "https://api.telegram.org/bottest-token/sendVideo",
                    httpRequest.uri().toString()
            );

            assertEquals("POST", httpRequest.method());

            String contentType = httpRequest.headers()
                    .firstValue("Content-Type")
                    .orElseThrow();

            assertTrue(
                    contentType.startsWith("multipart/form-data; boundary=")
            );

            String body = readRequestBody(httpRequest);

            assertTrue(body.contains("name=\"chat_id\""));
            assertTrue(body.contains("456"));

            assertTrue(body.contains("name=\"duration\""));
            assertTrue(body.contains("120"));

            assertTrue(body.contains("name=\"width\""));
            assertTrue(body.contains("1920"));

            assertTrue(body.contains("name=\"height\""));
            assertTrue(body.contains("1080"));

            assertTrue(body.contains("name=\"has_spoiler\""));
            assertTrue(body.contains("true"));

            assertTrue(
                    body.contains("name=\"show_caption_above_media\"")
            );
            assertTrue(body.contains("true"));

            assertTrue(body.contains("name=\"caption\""));
            assertTrue(body.contains("Test video"));

            assertTrue(body.contains("name=\"video\""));
            assertTrue(body.contains("filename=\"video.mp4\""));
            assertTrue(body.contains("Content-Type: video/mp4"));
            assertTrue(body.contains("fake video data"));
        }
    }

    @Nested
    @DisplayName("sendAudio")
    class SendAudio {

        @Test
        void shouldReturnMessage() throws Exception {
            mockResponse("""
            {
              "ok": true,
              "result": {
                "message_id": 123,
                "chat": {
                  "id": 456
                },
                "caption": "Test audio"
              }
            }
            """);

            SendAudioRequest request =
                    SendAudioRequest.builder()
                            .chatId(456L)
                            .audio("https://example.com/audio.mp3")
                            .duration(120)
                            .caption("Test audio")
                            .build();

            TelegramResponse<Message> response =
                    telegramClient.sendAudio(request);

            assertNotNull(response);
            assertTrue(response.ok());
            assertNotNull(response.result());
            assertEquals(123, response.result().messageId());
        }

        @Test
        void shouldSendCorrectRequest() throws Exception {
            mockResponse("""
            {
              "ok": true,
              "result": {
                "message_id": 123,
                "chat": {
                  "id": 456
                }
              }
            }
            """);

            SendAudioRequest request =
                    SendAudioRequest.builder()
                            .chatId(456L)
                            .audio("https://example.com/audio.mp3")
                            .duration(120)
                            .caption("Test audio")
                            .build();

            telegramClient.sendAudio(request);

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest httpRequest = captor.getValue();

            assertEquals(
                    "https://api.telegram.org/bottest-token/sendAudio",
                    httpRequest.uri().toString()
            );

            assertEquals("POST", httpRequest.method());

            assertEquals(
                    "application/json",
                    httpRequest.headers()
                            .firstValue("Content-Type")
                            .orElseThrow()
            );

            JsonNode body =
                    jsonMapper.readTree(readRequestBody(httpRequest));

            assertEquals(456, body.get("chat_id").asLong());
            assertEquals(
                    "https://example.com/audio.mp3",
                    body.get("audio").asString()
            );
            assertEquals(120, body.get("duration").asInt());
            assertEquals(
                    "Test audio",
                    body.get("caption").asString()
            );
        }
    }

    @Nested
    @DisplayName("sendAudio upload")
    class SendAudioUpload {

        @Test
        void shouldReturnMessage() throws Exception {
            mockResponse("""
            {
              "ok": true,
              "result": {
                "message_id": 123,
                "chat": {
                  "id": 456
                }
              }
            }
            """);

            InputFile audio = new InputFile(
                    "fake audio data".getBytes(StandardCharsets.UTF_8),
                    "audio.mp3",
                    "audio/mpeg"
            );

            SendAudioUploadRequest request =
                    SendAudioUploadRequest.builder()
                            .chatId(456L)
                            .audio(audio)
                            .duration(120)
                            .build();

            TelegramResponse<Message> response =
                    telegramClient.sendAudio(request);

            assertNotNull(response);
            assertTrue(response.ok());
            assertNotNull(response.result());
            assertEquals(123, response.result().messageId());
        }

        @Test
        void shouldSendCorrectMultipartRequest() throws Exception {
            mockResponse("""
            {
              "ok": true,
              "result": {
                "message_id": 123,
                "chat": {
                  "id": 456
                }
              }
            }
            """);

            byte[] audioData =
                    "fake audio data".getBytes(StandardCharsets.UTF_8);

            InputFile audio = new InputFile(
                    audioData,
                    "audio.mp3",
                    "audio/mpeg"
            );

            SendAudioUploadRequest request =
                    SendAudioUploadRequest.builder()
                            .chatId(456L)
                            .audio(audio)
                            .duration(120)
                            .build();

            telegramClient.sendAudio(request);

            ArgumentCaptor<HttpRequest> captor =
                    ArgumentCaptor.forClass(HttpRequest.class);

            verify(httpClient).send(
                    captor.capture(),
                    any(HttpResponse.BodyHandler.class)
            );

            HttpRequest httpRequest = captor.getValue();

            assertEquals(
                    "https://api.telegram.org/bottest-token/sendAudio",
                    httpRequest.uri().toString()
            );

            assertEquals("POST", httpRequest.method());

            String contentType = httpRequest.headers()
                    .firstValue("Content-Type")
                    .orElseThrow();

            assertTrue(
                    contentType.startsWith("multipart/form-data; boundary=")
            );

            String body = readRequestBody(httpRequest);

            assertTrue(body.contains("name=\"chat_id\""));
            assertTrue(body.contains("456"));

            assertTrue(body.contains("name=\"duration\""));
            assertTrue(body.contains("120"));

            assertTrue(body.contains("name=\"audio\""));
            assertTrue(body.contains("filename=\"audio.mp3\""));
            assertTrue(body.contains("Content-Type: audio/mpeg"));
            assertTrue(body.contains("fake audio data"));
        }
    }

    private String readRequestBody(HttpRequest request) {
        return new String(
                readRequestBodyBytes(request),
                StandardCharsets.UTF_8
        );
    }

    private byte[] readBody(HttpRequest request) {
        return readRequestBodyBytes(request);
    }

    private byte[] readRequestBodyBytes(HttpRequest request) {
        var output = new ByteArrayOutputStream();

        request.bodyPublisher()
                .orElseThrow()
                .subscribe(new Flow.Subscriber<>() {

                    @Override
                    public void onSubscribe(Flow.Subscription subscription) {
                        subscription.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(ByteBuffer item) {
                        try {
                            output.write(item.array());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throw new RuntimeException(throwable);
                    }

                    @Override
                    public void onComplete() {
                    }
                });

        return output.toByteArray();
    }

    private void mockHttpResponse(String responseBody) throws Exception {
        when(httpResponse.body()).thenReturn(
                responseBody.getBytes(StandardCharsets.UTF_8)
        );

        when(httpClient.send(
                any(HttpRequest.class),
                any(LimitedBodyHandler.class)
        )).thenReturn(httpResponse);
    }
}