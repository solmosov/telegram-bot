package io.github.solmosov.telegrambot.client;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class RateLimiterTest {

    @Test
    void shouldAllowRequestsUpToGlobalCapacity() throws InterruptedException {
        RateLimiter rateLimiter = new RateLimiter(3);

        try {
            rateLimiter.acquire(1L);
            rateLimiter.acquire(2L);
            rateLimiter.acquire(3L);

            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

            Future<?> future = executor.submit(() -> {
                try {
                    rateLimiter.acquire(4L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });

            assertThrows(TimeoutException.class, () -> future.get(100, TimeUnit.MILLISECONDS));

            future.cancel(true);
            executor.shutdownNow();
        } finally {
            rateLimiter.shutdown();
        }
    }

    @Test
    void shouldBlockSecondRequestFromSameChat() throws InterruptedException {
        RateLimiter rateLimiter = new RateLimiter(10);

        try {
            rateLimiter.acquire(1L);

            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

            Future<?> future = executor.submit(() -> {
                try {
                    rateLimiter.acquire(1L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });

            assertThrows(
                    TimeoutException.class,
                    () -> future.get(100, TimeUnit.MILLISECONDS)
            );

            future.cancel(true);
            executor.shutdownNow();
        } finally {
            rateLimiter.shutdown();
        }
    }

    @Test
    void shouldAllowRequestsFromDifferentChats() throws InterruptedException {
        RateLimiter rateLimiter = new RateLimiter(10);

        try {
            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

            Future<?> first = executor.submit(() -> {
                try {
                    rateLimiter.acquire(1L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });

            Future<?> second = executor.submit(() -> {
                try {
                    rateLimiter.acquire(2L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });

            assertDoesNotThrow(() -> first.get(1, TimeUnit.SECONDS));
            assertDoesNotThrow(() -> second.get(1, TimeUnit.SECONDS));

            executor.shutdown();
        } finally {
            rateLimiter.shutdown();
        }
    }

    @Test
    void shouldRefillGlobalPermitsAfterOneSecond() throws InterruptedException {
        RateLimiter rateLimiter = new RateLimiter(1);

        try {
            rateLimiter.acquire(1L);

            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

            Future<?> future = executor.submit(() -> {
                try {
                    rateLimiter.acquire(2L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });

            assertThrows(
                    TimeoutException.class,
                    () -> future.get(100, TimeUnit.MILLISECONDS)
            );

            assertDoesNotThrow(
                    () -> future.get(2, TimeUnit.SECONDS)
            );

            executor.shutdown();
        } finally {
            rateLimiter.shutdown();
        }
    }

    @Test
    void shouldRefillPerChatPermitAfterOneSecond() throws InterruptedException {
        RateLimiter rateLimiter = new RateLimiter(10);

        try {
            rateLimiter.acquire(1L);

            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

            Future<?> future = executor.submit(() -> {
                try {
                    rateLimiter.acquire(1L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });

            assertThrows(
                    TimeoutException.class,
                    () -> future.get(100, TimeUnit.MILLISECONDS)
            );

            assertDoesNotThrow(
                    () -> future.get(2, TimeUnit.SECONDS)
            );

            executor.shutdown();
        } finally {
            rateLimiter.shutdown();
        }
    }

    @Test
    void shouldInterruptWaitingAcquire() throws InterruptedException {
        RateLimiter rateLimiter = new RateLimiter(1);

        try {
            rateLimiter.acquire(1L);

            Thread thread = Thread.ofVirtual().start(() -> {
                assertThrows(
                        InterruptedException.class,
                        () -> rateLimiter.acquire(2L)
                );
            });

            Thread.sleep(100);

            thread.interrupt();
            thread.join(1000);

            assertFalse(thread.isAlive());
        } finally {
            rateLimiter.shutdown();
        }
    }

    @Test
    void shouldShutdownSuccessfully() {
        RateLimiter rateLimiter = new RateLimiter(10);

        assertDoesNotThrow(rateLimiter::shutdown);
    }
}