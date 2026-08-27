package io.github.shahbozolmosov.telegrambot.context.builder;

import io.github.shahbozolmosov.telegrambot.client.TelegramClient;
import io.github.shahbozolmosov.telegrambot.model.TelegramResponse;
import io.github.shahbozolmosov.telegrambot.request.chatAction.SendChatActionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatActionBuilder extends AbstractMessageBuilder<Boolean> {

    private static final Logger log = LoggerFactory.getLogger(ChatActionBuilder.class);

    private long chatId;
    private SendChatActionRequest.Action action;

    public ChatActionBuilder(
            TelegramClient client,
            Long updateId,
            Long defaultChatId
            ) {
        super(client, updateId);
        this.chatId = defaultChatId;
    }

    public ChatActionBuilder toChat(long chatId) {
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
        log.debug("Sending  chat action to updateId: {} chatId: {}", getUpdateId(), chatId);

        SendChatActionRequest request = new SendChatActionRequest(
                chatId,
                action
        );

        return client.sendChatAction(request);
    }
}
