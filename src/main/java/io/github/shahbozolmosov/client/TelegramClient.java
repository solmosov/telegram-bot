package io.github.shahbozolmosov.client;

import io.github.shahbozolmosov.exception.TelegramApiException;
import io.github.shahbozolmosov.exception.TelegramClientException;
import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.model.User;
import tools.jackson.core.StreamReadConstraints;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import javax.xml.stream.events.Characters;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public final class TelegramClient {

    private static final String API_BASE_URL = "https://api.telegram.org";

    private final String botToken;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private static final long MAX_RESPONSE_SIZE = 10 * 1024 * 1024; // 10 mb
    private static final int MAX_NESTING_DEPTH = 100;

    public TelegramClient(String botToken) {
        this.botToken = botToken;
        this.httpClient = HttpClient.newHttpClient();


        // JACKSON
        StreamReadConstraints constraints = StreamReadConstraints.builder()
                .maxNestingDepth(MAX_NESTING_DEPTH)
                .build();
        JsonFactory jsonFactory = JsonFactory.builder()
                .streamReadConstraints(constraints)
                .build();
        this.objectMapper = JsonMapper.builder(jsonFactory)
                .build();
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
                .GET()
                .build();


        return execute(
                request,
                new TypeReference<TelegramResponse<List<Update>>>() {
                }
        );
    }

    public TelegramResponse<Message> sendMessage(long chatId, String text) {
        String url = API_BASE_URL + "/bot" + botToken + "/sendMessage";

        String jsonBody = generateBody(chatId, text);

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

    private <T> T execute(
            HttpRequest request,
            TypeReference<T> typeReference
    ) {
        String responseBody = execute(request);

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
                    HttpResponse.BodyHandlers.ofByteArray()
            );

            if (response.statusCode() != 200) {
                throw new IOException(
                        "Telegram API returned HTTP status: " + response.statusCode()
                );
            }

            byte[] body = response.body();

            if (body.length > MAX_RESPONSE_SIZE) {
                throw new TelegramClientException(
                        "Telegram API response is too large: " + body.length + "bytes"
                );
            }

            return new String(body, StandardCharsets.UTF_8);
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

    private String generateBody(long chatId, String text) {
        Map<String, Object> body = Map.of(
                "chat_id", chatId,
                "text", text
        );

        return objectMapper.writeValueAsString(body);
    }
}
