package io.github.shahbozolmosov.telegrambot.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractRequest {
    @JsonProperty("chat_id")
    String chatId;

    @JsonProperty("allow_paid_broadcast")
    Boolean allowPaidBroadcast;

    @JsonProperty("protect_content")
    Boolean protectContent;

    @JsonProperty("disable_notification")
    Boolean disableNotification;
}
