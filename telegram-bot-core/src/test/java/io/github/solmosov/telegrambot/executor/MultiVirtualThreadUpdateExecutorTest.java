package io.github.solmosov.telegrambot.executor;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class MultiVirtualThreadUpdateExecutorTest {

    @Test
    void shouldRejectNonPositiveProcessingTimeout() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MultiVirtualThreadUpdateExecutor(0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new MultiVirtualThreadUpdateExecutor(-1)
        );
    }

    @Test
    void shouldExecuteSubmittedTask() throws InterruptedException {
        MultiVirtualThreadUpdateExecutor executor =
                new MultiVirtualThreadUpdateExecutor(5);

        try {
            CountDownLatch executed = new CountDownLatch(1);

            executor.submit(1L, executed::countDown);

            assertTrue(
                    executed.await(1, TimeUnit.SECONDS),
                    "Task was not executed"
            );
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void shouldExecuteTasksForSameChatSequentially() throws InterruptedException {
        MultiVirtualThreadUpdateExecutor executor =
                new MultiVirtualThreadUpdateExecutor(5);

        try {
            AtomicInteger running = new AtomicInteger();
            AtomicInteger maxRunning = new AtomicInteger();

            CountDownLatch firstStarted = new CountDownLatch(1);
            CountDownLatch releaseFirst = new CountDownLatch(1);
            CountDownLatch secondExecuted = new CountDownLatch(1);

            executor.submit(1L, () -> {
                int current = running.incrementAndGet();
                maxRunning.updateAndGet(max -> Math.max(max, current));

                firstStarted.countDown();

                try {
                    releaseFirst.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    running.decrementAndGet();
                }
            });

            assertTrue(firstStarted.await(1, TimeUnit.SECONDS));

            executor.submit(1L, () -> {
                int current = running.incrementAndGet();
                maxRunning.updateAndGet(max -> Math.max(max, current));

                try {
                    secondExecuted.countDown();
                } finally {
                    running.decrementAndGet();
                }
            });

            // Second task must wait for the first task.
            assertFalse(
                    secondExecuted.await(200, TimeUnit.MILLISECONDS),
                    "Tasks for the same chat must not run concurrently"
            );

            releaseFirst.countDown();

            assertTrue(
                    secondExecuted.await(1, TimeUnit.SECONDS),
                    "Second task was not executed"
            );

            assertEquals(1, maxRunning.get());
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void shouldExecuteTasksForDifferentChatsConcurrently()
            throws InterruptedException {

        MultiVirtualThreadUpdateExecutor executor =
                new MultiVirtualThreadUpdateExecutor(5);

        try {
            CountDownLatch bothStarted = new CountDownLatch(2);
            CountDownLatch release = new CountDownLatch(1);

            executor.submit(1L, () -> {
                bothStarted.countDown();

                try {
                    release.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            executor.submit(2L, () -> {
                bothStarted.countDown();

                try {
                    release.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            assertTrue(
                    bothStarted.await(1, TimeUnit.SECONDS),
                    "Tasks for different chats should run concurrently"
            );

            release.countDown();
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void shouldTimeoutLongRunningTask() throws InterruptedException {
        MultiVirtualThreadUpdateExecutor executor =
                new MultiVirtualThreadUpdateExecutor(1);

        try {
            CountDownLatch started = new CountDownLatch(1);
            CountDownLatch interrupted = new CountDownLatch(1);

            executor.submit(1L, () -> {
                started.countDown();

                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    interrupted.countDown();
                }
            });

            assertTrue(started.await(1, TimeUnit.SECONDS));

            assertTrue(
                    interrupted.await(3, TimeUnit.SECONDS),
                    "Task was not interrupted after timeout"
            );
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void shouldRejectTaskWhenQueueIsFull() throws InterruptedException {
        MultiVirtualThreadUpdateExecutor executor =
                new MultiVirtualThreadUpdateExecutor(60);

        try {
            CountDownLatch started = new CountDownLatch(1);
            CountDownLatch release = new CountDownLatch(1);

            executor.submit(1L, () -> {
                started.countDown();

                try {
                    release.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            assertTrue(started.await(1, TimeUnit.SECONDS));

            // First task is running.
            // 100 tasks can be queued.
            for (int i = 0; i < 100; i++) {
                executor.submit(1L, () -> {
                });
            }

            assertThrows(
                    RejectedExecutionException.class,
                    () -> executor.submit(1L, () -> {
                    })
            );

            release.countDown();
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void shouldRejectSubmissionAfterShutdown() {
        MultiVirtualThreadUpdateExecutor executor =
                new MultiVirtualThreadUpdateExecutor(5);

        executor.shutdown();

        assertThrows(
                RejectedExecutionException.class,
                () -> executor.submit(1L, () -> {
                })
        );
    }
}