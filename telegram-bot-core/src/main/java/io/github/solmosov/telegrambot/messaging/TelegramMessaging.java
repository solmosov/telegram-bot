package io.github.solmosov.telegrambot.messaging;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.messaging.builder.*;

public final class TelegramMessaging {


    private final TelegramClient client;

    public TelegramMessaging(TelegramClient client) {
        this.client = client;
    }

    public ChatActionBuilder chatAction(){
        return new ChatActionBuilder(
                client
        );
    }

    public MessageBuilder message(String textContent) {
        return new MessageBuilder(
                client,
                textContent
        );
    }

    public EditMessageTextBuilder editMessage(long messageId, String textContent){
        return new EditMessageTextBuilder(
                client,
                messageId,
                textContent
        );
    }

    public EditMessageCaptionBuilder editMessageCaption(long messageId, String caption){
        return new EditMessageCaptionBuilder(
                client,
                messageId,
                caption
        );
    }

    public DeleteMessageBuilder deleteMessage(long messageId){
        return new DeleteMessageBuilder(
                client,
                messageId
        );
    }

    public PhotoBuilder photo(String photoUrl){
        return new PhotoBuilder(
                client,
                photoUrl
        );
    }

}
