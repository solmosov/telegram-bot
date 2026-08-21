package io.github.shahbozolmosov.executor;

public interface UpdateExecutor {

    void submit(long chatId, Runnable task);

    void shutdown();
}
