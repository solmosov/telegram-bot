package io.github.solmosov.telegrambot.spring.boot;

import io.github.solmosov.telegrambot.bot.TelegramBotApplication;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class TelegramWebhookEndpointRegistrar implements SmartInitializingSingleton {
    private final TelegramBotApplication application;
    private final RequestMappingHandlerMapping handlerMapping;

    TelegramWebhookEndpointRegistrar(
            TelegramBotApplication application,
            RequestMappingHandlerMapping handlerMapping
    ) {
        this.application = application;
        this.handlerMapping = handlerMapping;
    }

    @Override
    public void afterSingletonsInstantiated() {
        Method method = resolveMethod();

        application.bots().forEach(bot ->
                bot.externalWebhookReceiver().ifPresent(receiver -> {
                    RequestMappingInfo mapping = RequestMappingInfo
                            .paths(receiver.path())
                            .methods(RequestMethod.POST)
                            .build();

                    handlerMapping.registerMapping(mapping, new TelegramWebhookEndpoint(receiver), method);
                })
        );
    }

    private Method resolveMethod() {
        try {
            return TelegramWebhookEndpoint.class.getMethod(
                    "receive",
                    String.class,
                    String.class,
                    byte[].class
            );
        } catch (NoSuchMethodException ex) {
            throw new IllegalStateException("Telegram webhook endpoint method not found", ex);
        }
    }
}
