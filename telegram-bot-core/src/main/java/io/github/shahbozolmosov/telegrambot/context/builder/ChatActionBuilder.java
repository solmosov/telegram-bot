package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.chatAction.SendChatActionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatActionBuilder {

    private static final Logger log = LoggerFactory.getLogger(ChatActionBuilder.class);
    private final TelegramClient client;
    private final Long updateId;

    private String chatId;
    private SendChatActionRequest.Action action;

    public ChatActionBuilder(
            TelegramClient client,
            Long updateId,
            String chatId
            ) {
        this.client = client;
        this.updateId = updateId;
        this.chatId = chatId;
    }

    public ChatActionBuilder toChat(String chatId) {
        this.chatId = chatId;
        return this;
    }
    
    public ChatActionBuilder typing() {
        this.action = SendChatActionRequest.Action.TYPING;
        return this;
    }

    public ChatActionBuilder uploadPhoto(){
        this.action = SendChatActionRequest.Action.UPLOAD_PHOTO;
        return this;
    }

    public ChatActionBuilder recordVideo() {
        this.action = SendChatActionRequest.Action.RECORD_VIDEO;
        return this;
    }

    public ChatActionBuilder uploadVideo() {
        this.action = SendChatActionRequest.Action.UPLOAD_VIDEO;
        return this;
    }

    public ChatActionBuilder recordVoice() {
        this.action = SendChatActionRequest.Action.RECORD_VOICE;
        return this;
    }

    public ChatActionBuilder uploadVoice() {
        this.action = SendChatActionRequest.Action.UPLOAD_VOICE;
        return this;
    }

    public ChatActionBuilder chooseSticker() {
        this.action = SendChatActionRequest.Action.CHOOSE_STICKER;
        return this;
    }

    public ChatActionBuilder findLocation() {
        this.action = SendChatActionRequest.Action.FIND_LOCATION;
        return this;
    }

    public ChatActionBuilder recordVideoNote() {
        this.action = SendChatActionRequest.Action.RECORD_VIDEO_NOTE;
        return this;
    }

    public ChatActionBuilder uploadVideoNote() {
        this.action = SendChatActionRequest.Action.UPLOAD_VIDEO_NOTE;
        return this;
    }

    public TelegramResponse<Boolean> send() {
        log.debug("Send chat action to updateId: {} chatId: {}", updateId == null ? "-" : updateId, chatId);

        SendChatActionRequest request = new SendChatActionRequest(
                chatId,
                action
        );

        return client.sendChatAction(request);
    }
}
