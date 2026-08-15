package io.github.shahbozolmosov.bot;

public class TelegramBotConfig {
    private final long shutdownTimeoutMillis;
    private final ExecutionMode executionMode;

    public TelegramBotConfig(long shutdownTimeoutMillis, ExecutionMode executionMode) {
        this.shutdownTimeoutMillis = shutdownTimeoutMillis;
        this.executionMode = executionMode;
    }

    public long getShutdownTimeoutMillis() {
        return shutdownTimeoutMillis;
    }

    public ExecutionMode getExecutionMode() {
        return executionMode;
    }

    public static TelegramBotConfig defaults() {
        return new TelegramBotConfig(5_000, ExecutionMode.SINGLE_THREAD);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long shutdownTimeoutMillis = 5_000;
        private ExecutionMode executionMode = ExecutionMode.SINGLE_THREAD;

        public Builder shutdownTimeout(long millis) {
            this.shutdownTimeoutMillis = millis;
            return this;
        }

        public Builder executionMode(ExecutionMode executionMode) {
            this.executionMode = executionMode;
            return this;
        }

        public TelegramBotConfig build() {
            return new TelegramBotConfig(shutdownTimeoutMillis, executionMode);
        }
    }
}
