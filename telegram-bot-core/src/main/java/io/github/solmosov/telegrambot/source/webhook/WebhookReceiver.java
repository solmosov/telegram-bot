package io.github.solmosov.telegrambot.source.webhook;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.context.BotContext;
import io.github.solmosov.telegrambot.dispatcher.Dispatcher;
import io.github.solmosov.telegrambot.exception.GlobalExceptionHandler;
import io.github.solmosov.telegrambot.executor.UpdateExecutor;
import io.github.solmosov.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class WebhookReceiver {

    private static final Logger log = LoggerFactory.getLogger(WebhookReceiver.class);

    public static final int MAX_REQUEST_BODY_SIZE = 256 * 1024;

    private static final long CACHE_EXPIRY_MS = 60000L;

    private final Map<Long, Long> processedUpdates = new ConcurrentHashMap<>();

    private final String botName;
    private final String path;
    private final byte[] secret;
    private final UpdateExecutor updateExecutor;
    private final Dispatcher dispatcher;
    private final JsonMapper jsonMapper;
    private final TelegramClient telegramClient;
    private final GlobalExceptionHandler globalExceptionHandler;

    private ScheduledExecutorService cleanupScheduler;

    public WebhookReceiver(
            String botName,
            String path,
            String secret,
            UpdateExecutor updateExecutor,
            Dispatcher dispatcher,
            JsonMapper jsonMapper,
            TelegramClient telegramClient,
            GlobalExceptionHandler globalExceptionHandler
    ) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException("Secret is required");
        }

        this.botName = botName;
        this.path = path;
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.updateExecutor = updateExecutor;
        this.dispatcher = dispatcher;
        this.jsonMapper = jsonMapper;
        this.telegramClient = telegramClient;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    public String path() {
        return path;
    }

    public void start() {
        cleanupScheduler = Executors.newScheduledThreadPool(1, r -> {
            Thread thread = new Thread(r, "WebhookCleanupThread");
            thread.setDaemon(true);
            return thread;
        });

        cleanupScheduler.scheduleAtFixedRate(this::cleanupExpiredEntries, 2, 2, TimeUnit.MINUTES);
    }

    public void shutdown() {
        if (cleanupScheduler != null) {
            cleanupScheduler.shutdown();
        }
    }

    public int receive(String receivedSecret, String contentType, byte[] body) {
        if (!isSecretValid(receivedSecret)) {
            return 403;
        }

        if (contentType == null || !contentType.toLowerCase().startsWith("application/json")) {
            return 415;
        }

        if (body == null || body.length == 0) {
            return 400;
        }

        if (body.length > MAX_REQUEST_BODY_SIZE) {
            return 413;
        }

        Update update;

        try {
            update = jsonMapper.readValue(body, Update.class);
        } catch (Exception ex) {
            log.warn("Invalid webhook request body", ex);
            return 400;
        }

        long updateId = update.updateId();

        if (!markAsNew(updateId)) {
            log.debug("Update {} already processed", updateId);
            return 200;
        }

        long chatId = extractChatId(update);

        try {
            updateExecutor.submit(chatId, () -> process(update));
        } catch (RejectedExecutionException ex) {
            processedUpdates.remove(updateId);
            log.warn("Update queue is full for chat {}", chatId);
            return 503;
        }

        return 200;
    }

    private void process(Update update) {
        MDC.put("bot", botName);
        try {
            BotContext context = new BotContext(telegramClient, update);

            try {
                log.debug("Processing update: {}", update.updateId());
                dispatcher.dispatch(update, context);
            } catch (Exception ex) {
                globalExceptionHandler.handle(ex, update, context);
            }
        } finally {
            MDC.remove("bot");
        }
    }

    private boolean isSecretValid(String receivedSecret) {
        return receivedSecret != null
                && !receivedSecret.isBlank()
                && MessageDigest.isEqual(receivedSecret.getBytes(StandardCharsets.UTF_8), secret);
    }

    private boolean markAsNew(long updateId) {
        long now = System.currentTimeMillis();

        Long previous = processedUpdates.putIfAbsent(updateId, now);

        if (previous == null) {
            return true;
        }

        if (now - previous > CACHE_EXPIRY_MS) {
            return processedUpdates.replace(updateId, previous, now);
        }

        return false;
    }

    private long extractChatId(Update update) {
        if (update.message() != null) {
            return update.message().chat().id();
        }

        if (update.callbackQuery() != null) {
            return update.callbackQuery().from().id();
        }

        return update.updateId();
    }

    private void cleanupExpiredEntries() {
        MDC.put("bot", botName);
        try {
            long now = System.currentTimeMillis();

            processedUpdates.entrySet().removeIf(entry -> now - entry.getValue() > CACHE_EXPIRY_MS);

            log.debug("Cleaned up expired entries from processedUpdates cache");
        } finally {
            MDC.remove("bot");
        }
    }
}