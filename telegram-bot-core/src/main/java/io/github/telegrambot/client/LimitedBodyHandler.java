package io.github.telegrambot.client;

import io.github.telegrambot.exception.client.TelegramClientException;

import java.net.http.HttpResponse;

public class LimitedBodyHandler implements HttpResponse.BodyHandler<byte[]> {

    private final long maxBytes;

    public LimitedBodyHandler(long maxBytes) {
        this.maxBytes = maxBytes;
    }

    @Override
    public HttpResponse.BodySubscriber<byte[]> apply(HttpResponse.ResponseInfo responseInfo) {
        long contentLength = responseInfo.headers()
                .firstValueAsLong("Content-Length")
                .orElse(-1);

        if (contentLength > maxBytes) {
            throw new TelegramClientException(
                    "Telegram API response is too large"
            );
        }

        return new LimitedBodySubscriber(maxBytes);
    }
}
