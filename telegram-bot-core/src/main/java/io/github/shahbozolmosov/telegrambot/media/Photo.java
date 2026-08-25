package io.github.shahbozolmosov.telegrambot.media;

import io.github.shahbozolmosov.telegrambot.model.InputFIle;
import io.github.shahbozolmosov.telegrambot.request.message.caption.SendPhotoRequest;
import io.github.shahbozolmosov.telegrambot.request.message.caption.SendPhotoUploadRequest;

public final class Photo {

    private Photo() {
    }

    public static SendPhotoRequest.Builder url(String url) {
        return SendPhotoRequest.builder()
                .photo(url);
    }

    public static SendPhotoUploadRequest.Builder file(byte[] photo, String name) {
        InputFIle inputFIle = new InputFIle(photo, name);
        return SendPhotoUploadRequest.builder()
                .photo(inputFIle);
    }

    public static SendPhotoUploadRequest.Builder file(byte[] photo, String name, String mimeType) {
        InputFIle inputFIle = new InputFIle(photo, name, mimeType);
        return SendPhotoUploadRequest.builder()
                .photo(inputFIle);
    }
}
