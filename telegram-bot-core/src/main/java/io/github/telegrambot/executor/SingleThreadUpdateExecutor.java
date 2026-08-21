package io.github.telegrambot.executor;

import java.util.concurrent.*;

public class SingleThreadUpdateExecutor implements UpdateExecutor {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService timeoutScheduler = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());

    private final long processingTimeout;

    public SingleThreadUpdateExecutor(long processingTimeout) {
        if (processingTimeout <= 0) {
            throw new IllegalArgumentException("Processing timeout must be greater than zero");
        }

        this.processingTimeout = processingTimeout;
    }

    @Override
    public void submit(long chatId, Runnable task) {
        Future<?> future = executor.submit(task);

        timeoutScheduler.schedule(
                () -> {
                    if (!future.isDone()) {
                        future.cancel(true);
                    }
                },
                processingTimeout,
                TimeUnit.SECONDS
        );
    }

    @Override
    public void shutdown() {
        timeoutScheduler.shutdown();
        executor.shutdown();
    }
}
