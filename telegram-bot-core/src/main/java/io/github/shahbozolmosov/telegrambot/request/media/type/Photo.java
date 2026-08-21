package io.github.shahbozolmosov.telegrambot.request.media.type;

import io.github.shahbozolmosov.telegrambot.request.media.send.SendPhotoRequest;

public final class Photo {

    private Photo() {
    }

    public static SendPhotoRequest.Builder photo(String url){
        return SendPhotoRequest.builder()
                .photo(url);
    }
}
