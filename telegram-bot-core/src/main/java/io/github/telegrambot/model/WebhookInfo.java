package io.github.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WebhookInfo(
        String url,

        @JsonProperty("has_custom_certificate")
        Boolean hasCustomCertificate,

        @JsonProperty("pending_update_count")
        Integer pendingUpdateCount,

        @JsonProperty("last_error_date")
        Long lastErrorDate,

        @JsonProperty("last_error_message")
        String lastErrorMessage,

        @JsonProperty("max_connections")
        Integer maxConnections,

        @JsonProperty("ip_address")
        String ipAddress
) {
}
