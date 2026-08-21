package io.github.shahbozolmosov.client;

import io.github.shahbozolmosov.client.http.MultipartBody;
import io.github.shahbozolmosov.client.http.MultipartBodyBuilder;
import io.github.shahbozolmosov.exception.api.TelegramApiException;
import io.github.shahbozolmosov.exception.client.TelegramClientException;
import io.github.shahbozolmosov.model.*;
import io.github.shahbozolmosov.request.media.send.*;
import io.github.shahbozolmosov.request.message.DeleteMessageRequest;
import io.github.shahbozolmosov.request.message.EditMessageReplyMarkupRequest;
import io.github.shahbozolmosov.request.message.EditMessageRequest;
import io.github.shahbozolmosov.request.message.SendMessageRequest;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public final class TelegramClient {

    private static final String API_BASE_URL = "https://api.telegram.org";

    private final String botToken;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final UpdateCountValidator updateCountValidator;
    private final MultipartBodyBuilder multipartBodyBuilder;
    private final RateLimiter rateLimiter;
    private final RateLimiter globalRateLimiter;

    private static final long MAX_RESPONSE_SIZE = 10 * 1024 * 1024; // 10 mb
    private static final int CONNECTION_TIMEOUT = 10; // 10 second
    private static final int TELEGRAM_API_TIMEOUT = 30; // 30 second
    private static final int MAX_UPDATES = 100;

    public TelegramClient(String botToken, JsonMapper jsonMapper) {
        this.botToken = botToken;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(CONNECTION_TIMEOUT))
                .build();

        this.rateLimiter = new RateLimiter(30);
        this.globalRateLimiter = new RateLimiter(30);


        // Json Mapper
        this.objectMapper = jsonMapper;

        this.updateCountValidator = new UpdateCountValidator(objectMapper, MAX_UPDATES);

        this.multipartBodyBuilder = new MultipartBodyBuilder(objectMapper);
    }

    public TelegramResponse<User> getMe() {
        String url = API_BASE_URL + "/bot" + botToken + "/getMe";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return execute(
                request,
                new TypeReference<TelegramResponse<User>>() {
                }
        );
    }

    public TelegramResponse<List<Update>> getUpdates(long offset) {
        String url = API_BASE_URL + "/bot" + botToken + "/getUpdates?offset=" + offset + "&timeout=" + TELEGRAM_API_TIMEOUT;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(TELEGRAM_API_TIMEOUT + 10))
                .GET()
                .build();

        var responseBody = execute(
                request,
                new TypeReference<TelegramResponse<List<Update>>>() {
                }
        );

        updateCountValidator.validate(responseBody.result());

        return responseBody;
    }


    // --------------------- Send Webhook ---------------------
    public TelegramResponse<Boolean> setWebhook(
            String webhookUrl,
            String secret
    ) {

        String url = API_BASE_URL + "/bot" + botToken + "/setWebhook";

        String jsonBody = objectMapper.writeValueAsString(Map.of(
                "url", webhookUrl,
                "secret_token", secret
        ));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return execute(request,
                new TypeReference<TelegramResponse<Boolean>>() {
                }
        );
    }


    public TelegramResponse<WebhookInfo> getWebhookInfo() {
        String url = API_BASE_URL + "/bot" + botToken + "/getWebhookInfo";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();


        return execute(
                request,
                new TypeReference<TelegramResponse<WebhookInfo>>() {
                }
        );
    }

    public TelegramResponse<Boolean> deleteWebhook() {
        String url = API_BASE_URL + "/bot" + botToken + "/deleteWebhook";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/jsons")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();


        return execute(
                request,
                new TypeReference<TelegramResponse<Boolean>>() {
                }
        );
    }

    // --------------------- Send Message ---------------------
    public TelegramResponse<Message> sendMessage(
            String chatId,
            String text
    ) {
        return sendMessage(
                new SendMessageRequest(chatId, text, null)
        );
    }

    public TelegramResponse<Message> sendMessage(
            SendMessageRequest requestBody
    ) {

        acquirePermit(requestBody.chatId());

        String url = API_BASE_URL + "/bot" + botToken + "/sendMessage";

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return execute(
                request,
                new TypeReference<TelegramResponse<Message>>() {
                }
        );
    }

    // --------------------- Edit Message ---------------------
    public TelegramResponse<Message> editMessage(
            String chatId,
            long messageId,
            String text
    ) {
        return editMessage(
                new EditMessageRequest(chatId, messageId, text, null)
        );
    }

    public TelegramResponse<Message> editMessage(
            EditMessageRequest requestBody
    ) {

        acquirePermit(requestBody.chatId());

        String url = API_BASE_URL + "/bot" + botToken + "/editMessageText";

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return execute(
                request,
                new TypeReference<TelegramResponse<Message>>() {
                }
        );
    }

    // --------------------- Edit Message Reply Markup ----------
    public TelegramResponse<Message> editMessageReplyMarkup(
            EditMessageReplyMarkupRequest requestBody
    ) {
        acquirePermit(requestBody.chatId());

        String url = API_BASE_URL + "/bot" + botToken + "/editMessageReplyMarkup";

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return execute(
                request,
                new TypeReference<TelegramResponse<Message>>() {
                }
        );
    }

    // --------------------- Delete Message ---------------------
    public TelegramResponse<Boolean> deleteMessage(
            DeleteMessageRequest requestBody
    ) {
        acquirePermit(requestBody.chatId());

        String url = API_BASE_URL + "/bot" + botToken + "/deleteMessage";

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return execute(
                request,
                new TypeReference<TelegramResponse<Boolean>>() {
                }
        );
    }

    // --------------------- Answer callback query ---------------------
    public TelegramResponse<Boolean> answerCallbackQuery(
            String callbackQueryId
    ) {

        acquirePermitGlobal();

        String url = API_BASE_URL + "/bot" + botToken + "/answerCallbackQuery";

        String jsonBody = objectMapper.writeValueAsString(
                Map.of(
                        "callback_query_id", callbackQueryId
                )
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return execute(
                request,
                new TypeReference<TelegramResponse<Boolean>>() {
                }
        );
    }

    public TelegramResponse<Boolean> answerCallbackQuery(
            String callbackQueryId,
            String text
    ) {

        acquirePermitGlobal();

        String url = API_BASE_URL + "/bot" + botToken + "/answerCallbackQuery";

        String jsonBody = objectMapper.writeValueAsString(
                Map.of(
                        "callback_query_id", callbackQueryId,
                        "text", text
                )
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return execute(
                request,
                new TypeReference<TelegramResponse<Boolean>>() {
                }
        );
    }

    // --------------------- Send Document ---------------------
    public TelegramResponse<Message> sendDocument(
            SendDocumentRequest requestBody
    ) {

        acquirePermit(requestBody.chatId());

        String url = API_BASE_URL + "/bot" + botToken + "/sendDocument";

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return execute(
                request,
                new TypeReference<TelegramResponse<Message>>() {
                }
        );
    }

    public TelegramResponse<Message> sendDocument(
            SendDocumentUploadRequest requestBody
    ) {
        acquirePermit(requestBody.chatId());

        String url = API_BASE_URL + "/bot" + botToken + "/sendDocument";

        MultipartBody multipartBody = multipartBodyBuilder.build(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", multipartBody.contentType())
                .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody.bytes()))
                .build();

        return execute(request, new TypeReference<TelegramResponse<Message>>() {
        });
    }

    // --------------------- Send Photo ---------------------
    public TelegramResponse<Message> sendPhoto(
            SendPhotoRequest requestBody
    ) {
        acquirePermit(requestBody.chatId());

        String url = API_BASE_URL + "/bot" + botToken + "/sendPhoto";

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return execute(
                request,
                new TypeReference<TelegramResponse<Message>>() {
                }
        );
    }

    // --------------------- Send Video ---------------------
    public TelegramResponse<Message> sendVideo(
            SendVideoRequest requestBody
    ) {
        acquirePermit(requestBody.chatId());

        String url = API_BASE_URL + "/bot" + botToken + "/sendVideo";

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();


        return execute(
                request,
                new TypeReference<TelegramResponse<Message>>() {
                }
        );
    }

    public TelegramResponse<Message> sendVideo(
            SendVideoUploadRequest requestBody
    ) {
        acquirePermit(requestBody.chatId());

        String url = API_BASE_URL + "/bot" + botToken + "/sendVideo";

        MultipartBody multipartBody = multipartBodyBuilder.build(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", multipartBody.contentType())
                .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody.bytes()))
                .build();


        return execute(
                request,
                new TypeReference<TelegramResponse<Message>>() {
                }
        );
    }

    /* ---------------------------------------------
                       HELPERS
    -------------------------------------------- */
    public void shutdown() {
        this.rateLimiter.shutdown();
        this.globalRateLimiter.shutdown();
    }

    private <T> T execute(
            HttpRequest request,
            TypeReference<T> typeReference
    ) {
        String responseBody = execute(request);

        try {
            T response = objectMapper.readValue(
                    responseBody,
                    typeReference
            );

            if (response instanceof TelegramResponse<?> telegramResponse && !telegramResponse.ok()) {
                throw new TelegramApiException(
                        telegramResponse.errorCode(),
                        telegramResponse.description()
                );
            }

            return response;
        } catch (JacksonException ex) {
            throw new TelegramClientException(
                    "Failed to parse Telegram API response.",
                    ex
            );
        }
    }

    private String execute(HttpRequest request) {
        try {
            HttpResponse<byte[]> response = httpClient.send(
                    request,
                    new LimitedBodyHandler(MAX_RESPONSE_SIZE)
            );


            byte[] body = response.body();

            if (body.length > MAX_RESPONSE_SIZE) {
                throw new TelegramClientException(
                        "Telegram API response is too large: " + body.length + "bytes"
                );
            }

            return new String(
                    body,
                    StandardCharsets.UTF_8
            );
        } catch (IOException ex) {
            throw new TelegramClientException(
                    "Failed to communicate with Telegram API",
                    ex
            );
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();

            throw new TelegramClientException(
                    "Telegram API request was interrupted",
                    ex
            );
        }
    }

    private void acquirePermit(String chatId) {
        try {
            rateLimiter.acquire(Long.parseLong(chatId));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new TelegramClientException("Interrupted while waiting for limiter", ex);
        }
    }

    private void acquirePermitGlobal() {
        try {
            globalRateLimiter.acquire(0L);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new TelegramClientException("Interrupted while waiting for limiter", ex);
        }
    }
}
