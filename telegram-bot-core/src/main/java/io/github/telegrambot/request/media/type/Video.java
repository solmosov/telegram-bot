package io.github.telegrambot.request.media.type;

import io.github.telegrambot.model.InputFIle;
import io.github.telegrambot.request.media.send.SendVideoRequest;
import io.github.telegrambot.request.media.send.SendVideoUploadRequest;

public final class Video {

    private Video() {
    }

    public static SendVideoRequest.Builder video(String url) {
        return SendVideoRequest.builder()
                .video(url);
    }

    public static SendVideoUploadRequest.Builder video(byte[] file, String name) {
        return SendVideoUploadRequest.builder()
                .video(new InputFIle(file, name));
    }

    public static SendVideoUploadRequest.Builder video(byte[] file, String name, String mimeType) {
        return SendVideoUploadRequest.builder()
                .video(new InputFIle(file, name, mimeType));
    }
}
