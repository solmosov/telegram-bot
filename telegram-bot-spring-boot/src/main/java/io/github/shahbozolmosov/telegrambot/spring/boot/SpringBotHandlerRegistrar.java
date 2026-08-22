package io.github.shahbozolmosov.telegrambot.spring.boot;

import io.github.shahbozolmosov.telegrambot.annotation.BotHandler;
import io.github.shahbozolmosov.telegrambot.scanner.HandlerRegistrar;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;

public class SpringBotHandlerRegistrar implements SmartInitializingSingleton {

    private final ListableBeanFactory beanFactory;
    private final HandlerRegistrar handlerRegistrar;

    public SpringBotHandlerRegistrar(
            ListableBeanFactory beanFactory,
            HandlerRegistrar handlerRegistrar
    ) {
        this.beanFactory = beanFactory;
        this.handlerRegistrar = handlerRegistrar;
    }

    @Override
    public void afterSingletonsInstantiated() {
        beanFactory.getBeansWithAnnotation(BotHandler.class)
                .values()
                .forEach(handlerRegistrar::register);
    }
}
