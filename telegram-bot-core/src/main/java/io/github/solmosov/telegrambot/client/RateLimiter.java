package io.github.solmosov.telegrambot.client;

import java.util.concurrent.*;

final class RateLimiter {

    private final Semaphore globalPermits;
    private final int globalCapacity;

    private final ConcurrentHashMap<Long, Semaphore> perChatPermits = new ConcurrentHashMap<>();
    private final int perChatCapacity = 1;

    private final ScheduledExecutorService refillScheduler = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());

    public RateLimiter(int globalRequestsPerSecond) {
        this.globalCapacity = globalRequestsPerSecond;
        this.globalPermits = new Semaphore(globalCapacity);

        refillScheduler.scheduleAtFixedRate(this::refillGlobal, 1, 1, TimeUnit.SECONDS);
        refillScheduler.scheduleAtFixedRate(this::refillPerChat, 1, 1, TimeUnit.SECONDS);
    }


    public void acquire(long chatId) throws InterruptedException {
        globalPermits.acquire();

        Semaphore chatSemaphore = perChatPermits.computeIfAbsent(
                chatId, id -> new Semaphore(perChatCapacity)
        );
        chatSemaphore.acquire();
    }

    public void shutdown() {
        refillScheduler.shutdown();
        try {
            if (!refillScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                refillScheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            refillScheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


    private void refillGlobal() {
        int missing = globalCapacity - globalPermits.availablePermits();
        if (missing > 0) {
            globalPermits.release(missing);
        }
    }

    private void refillPerChat() {
        perChatPermits.forEach((chatId, semaphore) -> {
            int missing = perChatCapacity - semaphore.availablePermits();
            if (missing > 0) {
                semaphore.release(missing);
            }
        });
    }

}
