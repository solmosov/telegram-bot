package io.github.solmosov.telegrambot.client;

import io.github.solmosov.telegrambot.exception.client.TelegramClientException;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Flow;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LimitedBodySubscriberTest {

    @Test
    void shouldRequestOneItemOnSubscribe() {
        LimitedBodySubscriber subscriber =
                new LimitedBodySubscriber(100);

        Flow.Subscription subscription = mock(Flow.Subscription.class);

        subscriber.onSubscribe(subscription);

        verify(subscription).request(1);
    }

    @Test
    void shouldReceiveBodySuccessfully() {
        LimitedBodySubscriber subscriber =
                new LimitedBodySubscriber(100);

        Flow.Subscription subscription = mock(Flow.Subscription.class);

        subscriber.onSubscribe(subscription);

        subscriber.onNext(List.of(
                ByteBuffer.wrap("Hello".getBytes())
        ));

        subscriber.onComplete();

        byte[] body = subscriber.getBody().toCompletableFuture().join();

        assertArrayEquals(
                "Hello".getBytes(),
                body
        );

        verify(subscription, times(2)).request(1);
    }

    @Test
    void shouldHandleMultipleBuffers() {
        LimitedBodySubscriber subscriber =
                new LimitedBodySubscriber(100);

        Flow.Subscription subscription = mock(Flow.Subscription.class);

        subscriber.onSubscribe(subscription);

        subscriber.onNext(List.of(
                ByteBuffer.wrap("Hello ".getBytes()),
                ByteBuffer.wrap("World".getBytes())
        ));

        subscriber.onComplete();

        byte[] body = subscriber.getBody().toCompletableFuture().join();

        assertArrayEquals(
                "Hello World".getBytes(),
                body
        );
    }

    @Test
    void shouldAcceptBodyWhenSizeEqualsLimit() {
        byte[] data = "Hello".getBytes();

        LimitedBodySubscriber subscriber =
                new LimitedBodySubscriber(data.length);

        Flow.Subscription subscription = mock(Flow.Subscription.class);

        subscriber.onSubscribe(subscription);
        subscriber.onNext(List.of(ByteBuffer.wrap(data)));
        subscriber.onComplete();

        assertArrayEquals(
                data,
                subscriber.getBody().toCompletableFuture().join()
        );
    }

    @Test
    void shouldFailWhenBodyExceedsLimit() {
        LimitedBodySubscriber subscriber =
                new LimitedBodySubscriber(5);

        Flow.Subscription subscription = mock(Flow.Subscription.class);

        subscriber.onSubscribe(subscription);

        subscriber.onNext(List.of(
                ByteBuffer.wrap("Hello!".getBytes())
        ));

        var future = subscriber.getBody().toCompletableFuture();

        assertTrue(future.isCompletedExceptionally());

        CompletionException exception = assertThrows(
                CompletionException.class,
                future::join
        );

        assertInstanceOf(
                TelegramClientException.class,
                exception.getCause()
        );

        assertEquals(
                "Telegram API response is too large",
                exception.getCause().getMessage()
        );

        verify(subscription).cancel();
    }

    @Test
    void shouldFailWhenCombinedBuffersExceedLimit() {
        LimitedBodySubscriber subscriber =
                new LimitedBodySubscriber(5);

        Flow.Subscription subscription = mock(Flow.Subscription.class);

        subscriber.onSubscribe(subscription);

        subscriber.onNext(List.of(
                ByteBuffer.wrap("Hel".getBytes()),
                ByteBuffer.wrap("lo!".getBytes())
        ));

        var future = subscriber.getBody().toCompletableFuture();

        assertTrue(future.isCompletedExceptionally());

        verify(subscription).cancel();
    }

    @Test
    void shouldCompleteExceptionallyWhenOnErrorCalled() {
        LimitedBodySubscriber subscriber =
                new LimitedBodySubscriber(100);

        RuntimeException error = new RuntimeException("Connection failed");

        subscriber.onError(error);

        var future = subscriber.getBody().toCompletableFuture();

        assertTrue(future.isCompletedExceptionally());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                future::join
        );

        assertEquals(
                "Connection failed",
                exception.getCause().getMessage()
        );
    }

    @Test
    void shouldCompleteWithEmptyBody() {
        LimitedBodySubscriber subscriber =
                new LimitedBodySubscriber(100);

        subscriber.onComplete();

        byte[] body = subscriber.getBody().toCompletableFuture().join();

        assertArrayEquals(new byte[0], body);
    }
}