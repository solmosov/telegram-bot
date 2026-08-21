package io.github.telegrambot.executor;

public interface UpdateExecutor {

    void submit(long chatId, Runnable task);

    void shutdown();
}
