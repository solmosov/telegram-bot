package io.github.solmosov.telegrambot.scanner.resolver;

import io.github.solmosov.telegrambot.annotation.MessageHandler;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.solmosov.telegrambot.registry.Registry;
import io.github.solmosov.telegrambot.model.MessageType;

import java.lang.reflect.Method;

public class MessageHandlerResolver implements HandlerAnnotationResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(MessageHandler.class);
    }

    @Override
    public void register(String botName, Method method, Handler handler, Registry registry) {
        MessageHandler message = method.getAnnotation(MessageHandler.class);

        String key = botName;

        String methodKey = message.value().isEmpty()
                ? ""
                : message.value();

        key += methodKey;

        MessageHandlerRegistration registration = new MessageHandlerRegistration(
                MessageType.TEXT,
                key,
                handler
        );

        registry.register(registration);
    }
}
