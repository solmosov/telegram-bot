package io.github.shahbozolmosov.media;

import io.github.shahbozolmosov.request.media.SendPhotoRequest;

public final class Photo {

    private Photo() {
    }

    public static SendPhotoRequest.Builder photo(String url){
        return SendPhotoRequest.builder()
                .photo(url);
    }
}
