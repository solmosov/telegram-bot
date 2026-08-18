package io.github.shahbozolmosov.bot;

public class TelegramBotConfig {
    private final long shutdownTimeoutMillis;
    private final UpdatesMode updatesMode;
    private final ExecutionMode executionMode;

    // Webhook configuration
    private final String webhookHost;
    private final int webhookPort;
    private final String webhookPath;
    private final String webhookUrl;
    private final String webhookSecret;

    private TelegramBotConfig(
            long shutdownTimeoutMillis,
            ExecutionMode executionMode,
            UpdatesMode updatesMode,

            String webhookHost,
            int webhookPort,
            String webhookPath,
            String webhookUrl,
            String webhookSecret
    ) {
        this.shutdownTimeoutMillis = shutdownTimeoutMillis;
        this.executionMode = executionMode;
        this.updatesMode = updatesMode;

        this.webhookHost = webhookHost;
        this.webhookPath = webhookPath;
        this.webhookUrl = webhookUrl;
        this.webhookPort = webhookPort;
        this.webhookSecret = webhookSecret;
    }

    public static TelegramBotConfig defaults() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
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


    // Webhook methods
    public String getWebhookHost() {
        return webhookHost;
    }

    public String getWebhookPath() {
        return webhookPath;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public int getWebhookPort() {
        return webhookPort;
    }

    public String getWebhookSecret() {
        return webhookSecret;
    }


    public static class Builder {
        private long shutdownTimeoutMillis = 5_000;
        private ExecutionMode executionMode = ExecutionMode.SINGLE_THREAD;
        private UpdatesMode updatesMode = UpdatesMode.POLLING;

        private String webhookHost = "0.0.0.0";
        private int webhookPort = 8080;
        private String webhookPath;
        private String webhookUrl;
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

        // Webhook methods
        public Builder webhookHost(String host) {
            this.webhookHost = host;
            return this;
        }

        public Builder webhookPort(int webhookPort) {
            this.webhookPort = webhookPort;
            return this;
        }

        public Builder webhookPath(String path) {
            this.webhookPath = path;
            return this;
        }

        public Builder webhookUrl(String url) {
            this.webhookUrl = url;
            return this;
        }

        public Builder webhookSecret(String webhookSecret) {
            this.webhookSecret = webhookSecret;
            return this;
        }

        public TelegramBotConfig build() {
            validateConfiguration();

            return new TelegramBotConfig(
                    shutdownTimeoutMillis,
                    executionMode,
                    updatesMode,

                    webhookHost,
                    webhookPort,
                    webhookPath,
                    webhookUrl,
                    webhookSecret
            );
        }

        private void validateConfiguration() {
            if (updatesMode == UpdatesMode.WEBHOOK) {
                if (webhookPath == null || webhookPath.isBlank()) {
                    throw new IllegalArgumentException(
                            "webhookPath is required for WEBHOOK mode (e.g., '/webhook')"
                    );
                }

                if (webhookUrl == null || webhookUrl.isBlank()) {
                    throw new IllegalArgumentException(
                            "webhookUrl is required for WEBHOOK mode (e.g., 'https')"
                    );
                }

                if (webhookPort <= 0 || webhookPort > 65535) {
                    throw new IllegalArgumentException(
                            "webhookPort must be valid (1-65535), got: " + webhookPort
                    );
                }
            }
        }
    }
}
