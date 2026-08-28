package io.github.solmosov.telegrambot.executor;

public interface UpdateExecutor {

    void submit(long chatId, Runnable task);

    void shutdown();
}
