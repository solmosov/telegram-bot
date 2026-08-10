package io.github.shahbozolmosov.client;

import java.net.http.HttpResponse;

public class LimitedBodyHandler implements HttpResponse.BodyHandler<byte[]> {

    private final long maxBytes;

    public LimitedBodyHandler(long maxBytes) {
        this.maxBytes = maxBytes;
    }

    @Override
    public HttpResponse.BodySubscriber<byte[]> apply(HttpResponse.ResponseInfo responseInfo) {
        return new LimitedBodySubscriber(maxBytes);
    }
}
