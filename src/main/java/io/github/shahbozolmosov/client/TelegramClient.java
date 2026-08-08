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

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

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

            TelegramResponse<User> telegramResponse = objectMapper.readValue(
                    response.body(),
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

    public TelegramResponse<List<Update>> getUpdates(long offset) {
        String url = API_BASE_URL + "/bot" + botToken + "/getUpdates?offset=" + offset + "&timeout=30";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        System.out.println("[TG API] getUpdates: " + url);
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

            TelegramResponse<List<Update>> telegramResponse = objectMapper.readValue(
                    response.body(),
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

    public TelegramResponse<Message> sendMessage(long chatId, String text) {
        String url = API_BASE_URL + "/bot" + botToken + "/sendMessage";

        String body = """
                {
                  "chat_id": %d,
                  "text": "%s"
                }
                """.formatted(chatId, text);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                throw new IOException(
                        "Telegram API returned HTTP status: " + response.statusCode() + " " + response.body()
                );
            }

            TelegramResponse<Message> telegramResponse = objectMapper.readValue(
                    response.body(),
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
}
