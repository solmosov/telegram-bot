package io.github.shahbozolmosov.telegrambot.bot;

import io.github.shahbozolmosov.telegrambot.authorization.AuthorizationProvider;
import io.github.shahbozolmosov.telegrambot.exception.handler.DefaultGlobalExceptionHandler;
import io.github.shahbozolmosov.telegrambot.exception.GlobalExceptionHandler;

public class TelegramBotConfig {
    private final long shutdownTimeoutMillis;
    private final long processingTimeout;
    private final UpdatesMode updatesMode;
    private final ExecutionMode executionMode;

    // Webhook configuration
    private final String webhookHost;
    private final int webhookPort;
    private final String webhookPath;
    private final String webhookUrl;
    private final String webhookPathSecret;
    private final String webhookSecret;

    // Authorization
    private final AuthorizationProvider authorizationProvider;

    // Exception
    private final GlobalExceptionHandler globalExceptionHandler;

    private TelegramBotConfig(
            long shutdownTimeoutMillis,
            long processingTimeout,
            ExecutionMode executionMode,
            UpdatesMode updatesMode,

            String webhookHost,
            int webhookPort,
            String webhookPath,
            String webhookUrl,
            String webhookPathSecret,
            String webhookSecret,

            AuthorizationProvider authorizationProvider,

            GlobalExceptionHandler globalExceptionHandler
    ) {
        this.shutdownTimeoutMillis = shutdownTimeoutMillis;
        this.processingTimeout = processingTimeout;
        this.executionMode = executionMode;
        this.updatesMode = updatesMode;

        this.webhookHost = webhookHost;
        this.webhookPath = webhookPath;
        this.webhookPathSecret = webhookPathSecret;
        this.webhookUrl = webhookUrl;
        this.webhookPort = webhookPort;
        this.webhookSecret = webhookSecret;

        this.authorizationProvider = authorizationProvider;

        this.globalExceptionHandler = globalExceptionHandler;
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

    public long getProcessingTimeout() {
        return processingTimeout;
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

    public String getWebhookPathSecret() {
        return webhookPathSecret;
    }

    public String getWebhookSecret() {
        return webhookSecret;
    }


    // Authorization
    public AuthorizationProvider getAuthorizationProvider() {
        return authorizationProvider;
    }

    // Exception
    public GlobalExceptionHandler getGlobalExceptionHandler() {
        return globalExceptionHandler;
    }


    public static class Builder {
        private long shutdownTimeoutMillis = 5_000; // 5s
        private long processingTimeout = 30; // 30s
        private ExecutionMode executionMode = ExecutionMode.SINGLE_THREAD;
        private UpdatesMode updatesMode = UpdatesMode.POLLING;

        private String webhookHost = "0.0.0.0";
        private int webhookPort = 8080;
        private String webhookPath;
        private String webhookUrl;
        private String webhookPathSecret = null;
        private String webhookSecret = null;

        // Authorization
        private AuthorizationProvider authorizationProvider;

        // Exception
        private GlobalExceptionHandler globalExceptionHandler = new DefaultGlobalExceptionHandler();

        public Builder shutdownTimeout(long millis) {
            this.shutdownTimeoutMillis = millis;
            return this;
        }

        public Builder processingTimeout(long second) {
            this.processingTimeout = second;
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

        public Builder webhookPathSecret(String secret) {
            this.webhookPathSecret = secret;
            return this;
        }

        public Builder webhookSecret(String secret) {
            this.webhookSecret = secret;
            return this;
        }

        // Authorization
        public Builder authorizationProvider(AuthorizationProvider authorizationProvider) {
            this.authorizationProvider = authorizationProvider;
            return this;
        }

        // Exception
        public Builder globalExceptionHandler(GlobalExceptionHandler globalExceptionHandler) {
            this.globalExceptionHandler = globalExceptionHandler;
            return this;
        }

        public TelegramBotConfig build() {
            validateConfiguration();

            return new TelegramBotConfig(
                    shutdownTimeoutMillis,
                    processingTimeout,
                    executionMode,
                    updatesMode,

                    webhookHost,
                    webhookPort,
                    webhookPath,
                    webhookUrl,
                    webhookPathSecret,
                    webhookSecret,

                    authorizationProvider,

                    globalExceptionHandler
            );
        }

        private void validateConfiguration() {
            if (updatesMode == UpdatesMode.WEBHOOK) {
                if (webhookPath == null || webhookPath.isBlank()) {
                    throw new IllegalArgumentException(
                            "webhookPath is required for WEBHOOK mode (e.g., '/webhook')"
                    );
                }

                if (webhookPathSecret == null || webhookPathSecret.isBlank()) {
                    throw new IllegalArgumentException(
                            "webhookPathSecret is required for WEBHOOK mode (e.g., 'your-generated-secret')"
                    );
                }

                if (webhookSecret == null || webhookSecret.isBlank()) {
                    throw new IllegalArgumentException(
                            "webhookSecret is required for WEBHOOK mode (e.g., 'your-generated-secret')"
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
