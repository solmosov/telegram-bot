package io.github.shahbozolmosov.telegrambot.source.webhook;

public final class WebhookUrl {

    private final String baseUrl;
    private final String path;
    private final String botName;
    private final String pathSecret;

    public WebhookUrl(
            String baseUrl,
            String path,
            String botName,
            String pathSecret
    ) {
        this.baseUrl = normalizeBaseUrl(baseUrl);
        this.path = normalizePath(path);
        this.botName = normalizeSegment(botName);
        this.pathSecret = normalizeSegment(pathSecret);
    }

    public String fullUrl() {
        return baseUrl + path + "/" + botName + "/" + pathSecret;
    }

    public String serverPath() {
        return path + "/" + botName + "/" + pathSecret;
    }


    // Normalizers
    private static String normalizeBaseUrl(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Webhook base URL is required");
        }

        return value.endsWith("/")
                ? value.substring(0, value.length() - 1)
                : value;
    }

    private static String normalizePath(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Webhook path is required");
        }

        if (!value.startsWith("/")) {
            value = "/" + value;
        }

        if (value.endsWith("/")) {
            value = value.substring(0, value.length() - 1);
        }

        return value;
    }

    public static String normalizeSegment(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Webhook path segment is required");
        }

        return value
                .replace("/", "")
                .trim();
    }
}
