package io.github.solmosov.telegrambot.spring.boot;

import io.github.solmosov.telegrambot.annotation.BotHandler;
import io.github.solmosov.telegrambot.bot.TelegramBotApplication;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;

class SpringBotHandlerRegistrar implements SmartInitializingSingleton {

    private final ListableBeanFactory beanFactory;
    private final TelegramBotApplication telegramBotApplication;

    public SpringBotHandlerRegistrar(
            ListableBeanFactory beanFactory,
            TelegramBotApplication application
    ) {
        this.beanFactory = beanFactory;
        this.telegramBotApplication = application;
    }

    @Override
    public void afterSingletonsInstantiated() {
        beanFactory.getBeansWithAnnotation(BotHandler.class)
                .values()
                .forEach(this::register);
    }

    private void register(Object handler) {
        BotHandler annotation = handler.getClass().getAnnotation(BotHandler.class);

        if (annotation == null) {
            return;
        }


        telegramBotApplication.getBot(annotation.value())
                .registerHandler(handler);
    }
}
