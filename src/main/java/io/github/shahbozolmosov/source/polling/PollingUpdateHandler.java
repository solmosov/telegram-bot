package io.github.shahbozolmosov.source.polling;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.exception.GlobalExceptionHandler;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.executor.UpdateExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class PollingUpdateHandler {

    private static final Logger log = LoggerFactory.getLogger(PollingUpdateHandler.class);

    private final String botName;

    private final TelegramClient client;
    private final Dispatcher dispatcher;
    private final UpdateExecutor updateExecutor;
    private final GlobalExceptionHandler globalExceptionHandler;

    public PollingUpdateHandler(
            String botName,
            TelegramClient client,
            Dispatcher dispatcher,
            UpdateExecutor updateExecutor,
            GlobalExceptionHandler globalExceptionHandler
    ) {
        this.botName = botName;
        this.client = client;
        this.dispatcher = dispatcher;
        this.updateExecutor = updateExecutor;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    public void handle(Update update) {
        long chatId = extractChatId(update);

        updateExecutor.submit(chatId, () -> {
            MDC.put("bot", botName);
            try {
                BotContext context = new BotContext(client, update);

                try {
                    log.debug("Processing update: {}", update.updateId());

                    dispatcher.dispatch(update, context);
                } catch (Exception ex) {
                    globalExceptionHandler.handle(ex, update, context);
                }
            } finally {
                MDC.remove("bot");
            }
        });
    }

    private long extractChatId(Update update) {
        if (update.message() != null) {
            return update.message().chat().id();
        }

        if (update.callbackQuery() != null) {
            return update.callbackQuery().message().chat().id();
        }

        return update.updateId();
    }

    public UpdateExecutor getExecutor() {
        return updateExecutor;
    }
}
