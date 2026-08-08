package io.github.shahbozolmosov.client;

import io.github.shahbozolmosov.exception.TelegramApiException;
import io.github.shahbozolmosov.exception.TelegramClientException;
import io.github.shahbozolmosov.model.TelegramResponse;
import io.github.shahbozolmosov.model.User;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
}
