package io.github.shahbozolmosov.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadUpdateExecutor implements UpdateExecutor{

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void submit(long chatId, Runnable task) {
        executor.submit(task);
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }
}
