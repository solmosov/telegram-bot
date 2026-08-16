package io.github.shahbozolmosov.bot;

public class TelegramBotConfig {
    private final long shutdownTimeoutMillis;
    private final ExecutionMode executionMode;
    private final UpdatesMode updatesMode;
    private final int webhookPort;
    private final String webhookSecret;


    public enum UpdatesMode {
        POLLING,
        WEBHOOK
    }

    public TelegramBotConfig(
            long shutdownTimeoutMillis,
            ExecutionMode executionMode,
            UpdatesMode updatesMode,
            int webhookPort,
            String webhookSecret
    ) {
        this.shutdownTimeoutMillis = shutdownTimeoutMillis;
        this.executionMode = executionMode;
        this.updatesMode = updatesMode;
        this.webhookPort = webhookPort;
        this.webhookSecret = webhookSecret;
    }

    public static TelegramBotConfig defaults() {
        return builder().build();
    }

    public long getShutdownTimeoutMillis() {
        return shutdownTimeoutMillis;
    }

    public ExecutionMode getExecutionMode() {
        return executionMode;
    }

    public UpdatesMode getUpdatesMode() {
        return updatesMode;
    }

    public int getWebHookPort() {
        return webhookPort;
    }

    public String getWebhookSecret() {
        return webhookSecret;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long shutdownTimeoutMillis = 5_000;
        private ExecutionMode executionMode = ExecutionMode.SINGLE_THREAD;
        private UpdatesMode updatesMode = UpdatesMode.POLLING;
        private int webhookPort = 8080;
        private String webhookSecret = null;

        public Builder shutdownTimeout(long millis) {
            this.shutdownTimeoutMillis = millis;
            return this;
        }

        public Builder executionMode(ExecutionMode executionMode) {
            this.executionMode = executionMode;
            return this;
        }

        public Builder updateMode(UpdatesMode updatesMode) {
            this.updatesMode = updatesMode;
            return this;
        }

        public Builder webhookPort(int webhookPort) {
            this.webhookPort = webhookPort;
            return this;
        }

        public Builder webhookSecret(String webhookSecret) {
            this.webhookSecret = webhookSecret;
            return this;
        }

        public TelegramBotConfig build() {
            return new TelegramBotConfig(
                    shutdownTimeoutMillis,
                    executionMode,
                    updatesMode,
                    webhookPort,
                    webhookSecret
            );
        }
    }
}
