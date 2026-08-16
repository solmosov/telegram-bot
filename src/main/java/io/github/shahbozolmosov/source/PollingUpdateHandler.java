package io.github.shahbozolmosov.source;

import io.github.shahbozolmosov.client.TelegramClient;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.dispatcher.Dispatcher;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.polling.UpdateExecutor;

public class PollingUpdateHandler {

    private final TelegramClient client;
    private final Dispatcher dispatcher;
    private final UpdateExecutor updateExecutor;

    public PollingUpdateHandler(
            TelegramClient client,
            Dispatcher dispatcher,
            UpdateExecutor updateExecutor
    ) {
        this.client = client;
        this.dispatcher = dispatcher;
        this.updateExecutor = updateExecutor;
    }

    public void handle(Update update) {
        long chatId = extractChatId(update);

        updateExecutor.submit(chatId, () -> {
            try {
                System.out.println("[Telegram Bot] Processing update: " + update.updateId());

                BotContext context = new BotContext(client, update);
                dispatcher.dispatch(update, context);
            } catch (Exception ex) {
                System.err.println("[Telegram Bot] Handler error for update "
                        + update.updateId() + ": " + ex.getMessage());
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
