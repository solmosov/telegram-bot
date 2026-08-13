package io.github.shahbozolmosov.client;

import io.github.shahbozolmosov.exception.TelegramApiException;
import io.github.shahbozolmosov.exception.TelegramClientException;
import io.github.shahbozolmosov.client.http.MultipartBody;
import io.github.shahbozolmosov.client.http.MultipartBodyBuilder;
import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.model.User;
import io.github.shahbozolmosov.request.EditMessageRequest;
import io.github.shahbozolmosov.request.SendMessageRequest;
import io.github.shahbozolmosov.request.media.*;
import tools.jackson.core.StreamReadConstraints;
import tools.jackson.core.json.JsonFactory;
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

    private static final long MAX_RESPONSE_SIZE = 10 * 1024 * 1024; // 10 mb
    private static final int MAX_NESTING_DEPTH = 100;
    private static final int MAX_STRING_LENGTH = 1_000_000;
    private static final int CONNECTION_TIMEOUT = 10; // 10 second
    private static final int GET_UPDATES_REQUEST_TIMEOUT = 40; // 40 second
    private static final int MAX_UPDATES = 100;

    public TelegramClient(String botToken) {
        this.botToken = botToken;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(CONNECTION_TIMEOUT))
                .build();


        // JACKSON
        StreamReadConstraints constraints = StreamReadConstraints.builder()
                .maxNestingDepth(MAX_NESTING_DEPTH)
                .maxStringLength(MAX_STRING_LENGTH)
                .build();
        JsonFactory jsonFactory = JsonFactory.builder()
                .streamReadConstraints(constraints)
                .build();
        this.objectMapper = JsonMapper.builder(jsonFactory)
                .build();

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
        String url = API_BASE_URL + "/bot" + botToken + "/getUpdates?offset=" + offset + "&timeout=30";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(GET_UPDATES_REQUEST_TIMEOUT))
                .GET()
                .build();


        return execute(
                request,
                new TypeReference<TelegramResponse<List<Update>>>() {
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
        String url = API_BASE_URL + "/bot" + botToken + "/sendMessage";

        String jsonBody = objectMapper.writeValueAsString(requestBody);
        System.out.println("[TelegramClient] jsonBody: " + jsonBody);

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

    // --------------------- Answer callback query ---------------------
    public TelegramResponse<Boolean> answerCallbackQuery(
            String callbackQueryId
    ) {
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
        String url = API_BASE_URL + "/bot" + botToken + "/sendPhoto";

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        System.out.println("caption ------> ");

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
    private <T> T execute(
            HttpRequest request,
            TypeReference<T> typeReference
    ) {
        String responseBody = execute(request);


        if (typeReference.getType().getTypeName().contains("TelegramResponse<java.util.List<io.github.shahbozolmosov.model.Update>>")) {
            updateCountValidator.validate(responseBody);
        }

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

            String responseBody = new String(
                    body,
                    StandardCharsets.UTF_8
            );

//            if (response.statusCode() != 200) {
//                System.err.println(
//                        "[TelegramClient] Telegram API error: "
//                                + responseBody
//                );
//
//                throw new IOException(
//                        "Telegram API returned HTTP status: " + response.statusCode()
//                );
//            }

            return responseBody;
        } catch (IOException ex) {
            throw new TelegramClientException(
                    "Failed to communicate with Telegram API",
                    ex
            );
        } catch (InterruptedException ex) {
            throw new TelegramClientException(
                    "Telegram API request was interrupted",
                    ex
            );
        }
    }
}
