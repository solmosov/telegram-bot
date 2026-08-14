package io.github.shahbozolmosov.polling;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class ChatSequentialExecutor {

    private static class ChatWorker {
        final ExecutorService executor = Executors.newSingleThreadExecutor(Thread.ofVirtual().factory());
        final AtomicLong lastUsedAt = new AtomicLong(System.currentTimeMillis());
    }

    private final ConcurrentHashMap<Long, ChatWorker> workers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());

    private static final long IDLE_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(10);


    public ChatSequentialExecutor() {
        cleaner.scheduleAtFixedRate(this::evictIdleWorkers, 5, 5, TimeUnit.MINUTES);
    }

    public void submit(long chatId, Runnable task) {
        ChatWorker worker = workers.computeIfAbsent(chatId, id -> new ChatWorker());
        worker.lastUsedAt.set(System.currentTimeMillis());

        worker.executor.submit(task);
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

    public void shutdown() {
        cleaner.shutdown();
        workers.values().forEach(worker -> worker.executor.shutdown());

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
}
