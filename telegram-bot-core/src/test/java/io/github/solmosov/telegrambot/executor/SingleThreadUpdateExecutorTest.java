package io.github.solmosov.telegrambot.executor;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class SingleThreadUpdateExecutorTest {

    @Test
    void shouldRejectNonPositiveProcessingTimeout() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new SingleThreadUpdateExecutor(0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new SingleThreadUpdateExecutor(-1)
        );
    }


    @Test
    void shouldExecuteSubmittedTask() throws InterruptedException {
        SingleThreadUpdateExecutor executor = new SingleThreadUpdateExecutor(5);

        try {
            CountDownLatch latch = new CountDownLatch(1);

            executor.submit(123L, latch::countDown);

            assertTrue(
                    latch.await(1, TimeUnit.SECONDS),
                    "Task was not executed"
            );
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void shouldCancelTaskAfterProcessingTimeout() throws InterruptedException {
        SingleThreadUpdateExecutor executor =
                new SingleThreadUpdateExecutor(1);

        try {
            CountDownLatch started = new CountDownLatch(1);
            CountDownLatch interrupted = new CountDownLatch(1);

            executor.submit(123L, () -> {
                started.countDown();

                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    interrupted.countDown();
                }
            });

            assertTrue(
                    started.await(1, TimeUnit.SECONDS),
                    "Task was not started"
            );

            assertTrue(
                    interrupted.await(3, TimeUnit.SECONDS),
                    "Task was not interrupted after timeout"
            );
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void shouldAllowNextTaskAfterPreviousTaskFinishes() throws InterruptedException {
        SingleThreadUpdateExecutor executor =
                new SingleThreadUpdateExecutor(5);

        try {
            CountDownLatch firstTask = new CountDownLatch(1);
            CountDownLatch secondTask = new CountDownLatch(1);

            executor.submit(123L, firstTask::countDown);
            executor.submit(123L, secondTask::countDown);

            assertTrue(firstTask.await(1, TimeUnit.SECONDS));
            assertTrue(secondTask.await(1, TimeUnit.SECONDS));
        } finally {
            executor.shutdown();
        }
    }


    @Test
    void shouldNotExecuteTaskAfterShutdown() throws InterruptedException {
        SingleThreadUpdateExecutor executor =
                new SingleThreadUpdateExecutor(5);

        executor.shutdown();

        AtomicBoolean executed = new AtomicBoolean(false);

        assertThrows(
                RuntimeException.class,
                () -> executor.submit(123L, () -> executed.set(true))
        );

        Thread.sleep(100);

        assertFalse(executed.get());
    }

}