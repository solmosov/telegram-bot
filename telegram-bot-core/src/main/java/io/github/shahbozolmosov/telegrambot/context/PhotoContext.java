package io.github.shahbozolmosov.telegrambot.context;

import io.github.shahbozolmosov.telegrambot.model.Message;
import io.github.shahbozolmosov.telegrambot.model.PhotoSize;

import java.util.List;

public final class PhotoContext {

    private final Message message;

    public PhotoContext(
            Message message
    ) {
        this.message = message;
    }

    public PhotoSize originalPhoto() {
        List<PhotoSize> sizes = message.photo();

        if (sizes == null || sizes.isEmpty()) {
            throw new IllegalStateException(
                    "originalPhoto() called but this update has no photo. "
                            + "Make sure this is only used inside a @Photo handler."
            );
        }

        PhotoSize largest = sizes.getFirst();

        for (PhotoSize size : sizes) {
            if (size.width() * size.height() > largest.width() * largest.height()) {
                largest = size;
            }
        }

        return largest;
    }

    public List<PhotoSize> all() {
        return message.photo();
    }

    public String caption() {
        return message.caption();
    }

}
