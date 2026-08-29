package io.github.solmosov.telegrambot.spring.boot;

import io.github.solmosov.telegrambot.bot.TelegramBotApplication;

import java.util.List;

class TelegramBotApplicationFactory {

    private final List<TelegramBotRegistration> registrations;

    public TelegramBotApplicationFactory(List<TelegramBotRegistration> registrations) {
        this.registrations = registrations;
    }

    public TelegramBotApplication create() {
        TelegramBotApplication application = new TelegramBotApplication();


        registrations.forEach(registration -> {
            application.register(
                    registration.botName(),
                    registration.token(),
                    registration.config()
            );
        });

        return application;
    }
}
