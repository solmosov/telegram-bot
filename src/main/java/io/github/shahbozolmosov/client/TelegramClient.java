package io.github.shahbozolmosov.client;

import io.github.shahbozolmosov.exception.TelegramApiException;
import io.github.shahbozolmosov.exception.TelegramClientException;
import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.model.User;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public final class TelegramClient {

    private static final String API_BASE_URL = "https://api.telegram.org";

    private final String botToken;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TelegramClient(String botToken) {
        this.botToken = botToken;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public TelegramResponse<User> getMe() {
        String url = API_BASE_URL + "/bot" + botToken + "/getMe";

        String responseBody = execute(
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build()
        );


        TelegramResponse<User> telegramResponse = objectMapper.readValue(
                responseBody,
                new TypeReference<TelegramResponse<User>>() {
                }
        );

        if (!telegramResponse.ok()) {
            throw new TelegramApiException(
                    telegramResponse.errorCode(),
                    telegramResponse.description()
            );
        }

        return telegramResponse;
    }

    public TelegramResponse<List<Update>> getUpdates(long offset) {
        String url = API_BASE_URL + "/bot" + botToken + "/getUpdates?offset=" + offset + "&timeout=30";

        String responseBody = execute(
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build()
        );


        TelegramResponse<List<Update>> telegramResponse = objectMapper.readValue(
                responseBody,
                new TypeReference<TelegramResponse<List<Update>>>() {
                }
        );

        if (!telegramResponse.ok()) {
            throw new TelegramApiException(
                    telegramResponse.errorCode(),
                    telegramResponse.description()
            );
        }

        return telegramResponse;
    }

    public TelegramResponse<Message> sendMessage(long chatId, String text) {
        String url = API_BASE_URL + "/bot" + botToken + "/sendMessage";

        String jsonBody = generateBody(chatId, text);

        String responseBody = execute(HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build());

        TelegramResponse<Message> telegramResponse = objectMapper.readValue(
                responseBody,
                new TypeReference<TelegramResponse<Message>>() {
                }
        );

        if (!telegramResponse.ok()) {
            throw new TelegramApiException(
                    telegramResponse.errorCode(),
                    telegramResponse.description()
            );
        }

        return telegramResponse;
    }

    private String execute(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                throw new IOException(
                        "Telegram API returned HTTP status: " + response.statusCode()
                );
            }

            return response.body();
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
