package io.github.shahbozolmosov.telegrambot.request.media.type;

import io.github.shahbozolmosov.telegrambot.model.InputFIle;
import io.github.shahbozolmosov.telegrambot.request.media.send.SendPhotoRequest;
import io.github.shahbozolmosov.telegrambot.request.media.send.SendPhotoUploadRequest;

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

    public static SendPhotoUploadRequest.Builder file(byte[] photo, String name, String type) {
        InputFIle inputFIle = new InputFIle(photo, name, type);
        return SendPhotoUploadRequest.builder()
                .photo(inputFIle);
    }
}
