package io.github.shahbozolmosov.telegrambot.media;

import io.github.shahbozolmosov.telegrambot.model.InputFile;
import io.github.shahbozolmosov.telegrambot.request.message.media.SendPhotoRequest;
import io.github.shahbozolmosov.telegrambot.request.message.media.SendPhotoUploadRequest;

public final class Photo {

    private Photo() {
    }

    public static SendPhotoRequest.Builder url(String url) {
        return SendPhotoRequest.builder()
                .photo(url);
    }

    public static SendPhotoUploadRequest.Builder file(byte[] photo, String name) {
        InputFile inputFIle = new InputFile(photo, name);
        return SendPhotoUploadRequest.builder()
                .photo(inputFIle);
    }

    public static SendPhotoUploadRequest.Builder file(byte[] photo, String name, String mimeType) {
        InputFile inputFIle = new InputFile(photo, name, mimeType);
        return SendPhotoUploadRequest.builder()
                .photo(inputFIle);
    }
}
