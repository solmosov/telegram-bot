package io.github.telegrambot.source;

public interface UpdateSource {
    void start();

    void stop();

    void shutdown();

    SourceType getSourceType();

    enum SourceType {
        POLLING, WEBHOOK
    }
}
