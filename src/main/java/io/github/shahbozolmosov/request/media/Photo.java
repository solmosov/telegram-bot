package io.github.shahbozolmosov.request.media;

public final class Photo {

    private Photo() {
    }

    public static SendPhotoRequest.Builder photo(String url){
        return SendPhotoRequest.builder()
                .photo(url);
    }
}
