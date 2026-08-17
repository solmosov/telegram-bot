package io.github.shahbozolmosov.source;

import tools.jackson.databind.json.JsonMapper;

public interface UpdateSource {
    void start();

    void stop();

    void shutdown();

    SourceType getSourceType();

    enum SourceType {
        POLLING, WEBHOOK
    }
}
