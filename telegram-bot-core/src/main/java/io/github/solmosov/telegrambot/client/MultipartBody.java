package io.github.solmosov.telegrambot.client;

import java.net.http.HttpRequest;

final class MultipartBody {

    private final String boundary;
    private final HttpRequest.BodyPublisher bodyPublisher;

    public MultipartBody(String boundary, HttpRequest.BodyPublisher bodyPublisher) {
        this.boundary = boundary;
        this.bodyPublisher = bodyPublisher;
    }

    public String contentType() {
        return "multipart/form-data; boundary=" + boundary;
    }

    public HttpRequest.BodyPublisher bodyPublisher() {
        return bodyPublisher;
    }
}
