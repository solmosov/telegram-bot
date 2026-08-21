package io.github.shahbozolmosov.telegrambot.executor;

public interface UpdateExecutor {

    void submit(long chatId, Runnable task);

    void shutdown();
}
