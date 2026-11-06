package io.github.solmosov.telegrambot.spring.boot;

import io.github.solmosov.telegrambot.source.webhook.WebhookReceiver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public class TelegramWebhookEndpoint {
    private final WebhookReceiver receiver;

    TelegramWebhookEndpoint(WebhookReceiver receiver) {
        this.receiver = receiver;
    }

    public ResponseEntity<Void> receive(
            @RequestHeader(name = "X-Telegram-Bot-Api-Secret-Token", required = false) String secret,
            @RequestHeader(name = HttpHeaders.CONTENT_TYPE, required = false) String contentType,
            @RequestBody(required = false) byte[] body
    ) {
        return ResponseEntity.status(receiver.receive(secret, contentType, body)).build();
    }
}
