package io.github.shahbozolmosov.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class TelegramClient {

    private static final String API_BASE_URL = "https://api.telegram.org";

    private final String botToken;
    private final HttpClient httpClient;

    public TelegramClient(String botToken) {
        this.botToken = botToken;
        this.httpClient = HttpClient.newHttpClient();
    }

    public String getMe() throws IOException, InterruptedException {
        String url = API_BASE_URL + "/bot" + botToken + "/getMe";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        return response.body();
    }
}
