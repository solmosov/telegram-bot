package io.github.solmosov.telegrambot.client;

import io.github.solmosov.telegrambot.exception.client.TelegramClientException;

import java.io.ByteArrayOutputStream;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;

final class LimitedBodySubscriber implements HttpResponse.BodySubscriber<byte[]> {

    private final long maxBytes;
    private final ByteArrayOutputStream outputStream;
    private final CompletableFuture<byte[]> future;

    private Flow.Subscription subscription;
    private long receivedByte;

    public LimitedBodySubscriber(long maxBytes) {
        this.maxBytes = maxBytes;
        this.outputStream = new ByteArrayOutputStream();
        this.future = new CompletableFuture<>();
    }

    @Override
    public CompletionStage<byte[]> getBody() {
        return future;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(List<ByteBuffer> buffers) {
        for (ByteBuffer buffer : buffers) {
            int remaining = buffer.remaining();

            receivedByte += remaining;

            if (receivedByte > maxBytes) {
                subscription.cancel();

                future.completeExceptionally(
                        new TelegramClientException(
                                "Telegram API response is too large"
                        )
                );

                return;
            }

            byte[] bytes = new byte[remaining];
            buffer.get(bytes);

            outputStream.writeBytes(bytes);
        }

        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        future.completeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        future.complete(outputStream.toByteArray());
    }
}
