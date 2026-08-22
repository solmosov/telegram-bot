package io.github.shahbozolmosov.telegrambot.bot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TelegramBotApplication {

    private final Map<String, TelegramBot> bots = new HashMap<>();

    public TelegramBotApplication register(
            String name,
            String token
    ) {
        return register(name, token, null);
    }

    public TelegramBotApplication register(
            String name,
            String token,
            TelegramBotConfig config
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Bot name must not be blank");
        }

        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Bot token must not be blank for bot: " + name);
        }

        TelegramBot bot;

        if (config != null) {
            bot = new TelegramBot(name, token, config);
        } else {
            bot = new TelegramBot(name, token);
        }

        TelegramBot existing = bots.putIfAbsent(name, bot);

        if (existing != null) {
            throw new IllegalStateException("Telegram bot already registered: " + name);
        }

        return this;
    }

    public TelegramBot getBot(String name) {
        TelegramBot bot = bots.get(name);

        if (bot == null) {
            throw new IllegalStateException("Telegram bot is not registered: " + name);
        }

        return bot;
    }

    public boolean containsBot(String name) {
        return bots.containsKey(name);
    }

    public Collection<TelegramBot> bots() {
        return bots.values();
    }

    public void start() {
        if (bots.isEmpty()) {
            throw new IllegalStateException("No telegram bots registered");
        }

        bots.values().forEach(TelegramBot::start);
    }

    public void stop() {
        bots.values().forEach(TelegramBot::stopBot);
    }
}
