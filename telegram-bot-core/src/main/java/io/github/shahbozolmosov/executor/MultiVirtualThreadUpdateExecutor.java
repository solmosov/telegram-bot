package io.github.shahbozolmosov.executor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class MultiVirtualThreadUpdateExecutor implements UpdateExecutor {

    private static final int MAX_QUEUED_UPDATES_PER_CHAT = 100;

    private static class ChatWorker {
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MICROSECONDS,
                new ArrayBlockingQueue<>(MAX_QUEUED_UPDATES_PER_CHAT),
                Thread.ofVirtual().factory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        final AtomicLong lastUsedAt = new AtomicLong(System.currentTimeMillis());
    }

    private final ConcurrentHashMap<Long, ChatWorker> workers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());
    private final ScheduledExecutorService timeoutScheduler = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());

    private final long processingTimeout;

    private static final long IDLE_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(10);

    public MultiVirtualThreadUpdateExecutor(long processingTimeout) {
        if (processingTimeout <= 0) {
            throw new IllegalArgumentException("Processing timeout must be greater than zero");
        }

        this.processingTimeout = processingTimeout;

        cleaner.scheduleAtFixedRate(this::evictIdleWorkers, 5, 5, TimeUnit.MINUTES);
    }


    @Override
    public void submit(long chatId, Runnable task) {
        ChatWorker worker = workers.computeIfAbsent(chatId, id -> new ChatWorker());
        worker.lastUsedAt.set(System.currentTimeMillis());

        Future<?> future;

        try {
            future = worker.executor.submit(task);
        } catch (RejectedExecutionException ex) {
            throw new RejectedExecutionException(
                    "Too many queued updates for chat: " + chatId,
                    ex
            );
        }

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
        cleaner.shutdown();
        timeoutScheduler.shutdown();

        workers.values().forEach(workers -> workers.executor.shutdown());

        for (ChatWorker worker : workers.values()) {
            try {
                if (!worker.executor.awaitTermination(30, TimeUnit.SECONDS)) {
                    worker.executor.shutdown();
                }
            } catch (InterruptedException ex) {
                worker.executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }


    private void evictIdleWorkers() {
        long now = System.currentTimeMillis();
        workers.entrySet().removeIf(entry -> {
            ChatWorker worker = entry.getValue();
            boolean idle = (now - worker.lastUsedAt.get()) > IDLE_TIMEOUT_MS;

            if (idle) {
                worker.executor.shutdown();
            }

            return idle;
        });
    }
}
