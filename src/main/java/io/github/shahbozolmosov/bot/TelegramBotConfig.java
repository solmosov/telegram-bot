package io.github.shahbozolmosov.bot;

public class TelegramBotConfig {
    private final long shutdownTimeoutMillis;

    public TelegramBotConfig(long shutdownTimeoutMillis) {
        this.shutdownTimeoutMillis = shutdownTimeoutMillis;
    }

    public long getShutdownTimeoutMillis() {
        return shutdownTimeoutMillis;
    }

    public static TelegramBotConfig defaults() {
        return new TelegramBotConfig(5_000);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long shutdownTimeoutMillis = 5_000;

        public Builder shutdownTimeout(long millis) {
            this.shutdownTimeoutMillis = millis;
            return this;
        }

        public TelegramBotConfig build() {
            return new TelegramBotConfig(shutdownTimeoutMillis);
        }
    }
}
