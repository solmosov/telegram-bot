package io.github.shahbozolmosov.telegrambot.request.message.caption;

import io.github.shahbozolmosov.telegrambot.request.message.AbstractRequest;

abstract class CaptionRequest extends AbstractRequest {



    public CaptionRequest(String chatId, Boolean allowPaidBroadcast, Boolean protectContent, Boolean disableNotification) {
        super(chatId, allowPaidBroadcast, protectContent, disableNotification);
    }
}
