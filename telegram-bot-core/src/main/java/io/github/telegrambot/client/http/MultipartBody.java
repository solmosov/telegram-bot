package io.github.telegrambot.client.http;

public final class MultipartBody {

    private final String boundary;
    private final byte[] body;

    public MultipartBody(String boundary, byte[] body) {
        this.boundary = boundary;
        this.body = body;
    }

    public String contentType() {
        return "multipart/form-data; boundary=" + boundary;
    }

    public byte[] bytes() {
        return body;
    }
}
