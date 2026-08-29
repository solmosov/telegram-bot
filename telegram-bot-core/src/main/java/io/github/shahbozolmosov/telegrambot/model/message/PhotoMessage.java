package io.github.shahbozolmosov.telegrambot.model.message;

import io.github.shahbozolmosov.telegrambot.model.Chat;
import io.github.shahbozolmosov.telegrambot.model.From;
import io.github.shahbozolmosov.telegrambot.model.PhotoSize;

import java.util.List;

public final class PhotoMessage extends AbstractMessage {
    private final List<PhotoSize> photo;
    private final String caption;

    PhotoMessage(
            long messageId,
            From from,
            Chat chat,
            long date,
            List<PhotoSize> photo,
            String caption
    ) {
        super(messageId, from, chat, date);
        this.photo = photo;
        this.caption = caption;
    }

    public List<PhotoSize> photo() {
        return photo;
    }

    public String caption() {
        return caption;
    }
}
