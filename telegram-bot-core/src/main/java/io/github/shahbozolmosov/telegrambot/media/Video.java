package io.github.shahbozolmosov.telegrambot.media;

import io.github.shahbozolmosov.telegrambot.model.InputFIle;
import io.github.shahbozolmosov.telegrambot.request.message.media.SendVideoRequest;
import io.github.shahbozolmosov.telegrambot.request.message.media.SendVideoUploadRequest;

public final class Video {

    private Video() {
    }

    public static SendVideoRequest.Builder url(String url) {
        return SendVideoRequest.builder()
                .video(url);
    }

    public static SendVideoUploadRequest.Builder file(byte[] file, String name) {
        return SendVideoUploadRequest.builder()
                .video(new InputFIle(file, name));
    }

    public static SendVideoUploadRequest.Builder file(byte[] file, String name, String mimeType) {
        return SendVideoUploadRequest.builder()
                .video(new InputFIle(file, name, mimeType));
    }
}
