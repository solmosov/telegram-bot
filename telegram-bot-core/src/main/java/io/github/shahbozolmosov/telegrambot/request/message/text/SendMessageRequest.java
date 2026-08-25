package io.github.shahbozolmosov.telegrambot.request.message.text;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendMessageRequest extends TextRequest {

    public SendMessageRequest(String chatId, Boolean allowPaidBroadcast, Boolean protectContent, Boolean disableNotification) {
        super(chatId, allowPaidBroadcast, protectContent, disableNotification);
    }
}
