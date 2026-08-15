package io.github.shahbozolmosov.polling;

public interface UpdateExecutor {

    void submit(long chatId, Runnable task);

    void shutdown();
}
