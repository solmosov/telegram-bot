package io.github.shahbozolmosov.media;

import io.github.shahbozolmosov.model.InputFIle;
import io.github.shahbozolmosov.request.media.SendVideoRequest;
import io.github.shahbozolmosov.request.media.SendVideoUploadRequest;

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
