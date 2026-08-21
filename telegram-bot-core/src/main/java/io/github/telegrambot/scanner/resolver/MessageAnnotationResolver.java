package io.github.telegrambot.scanner.resolver;

import io.github.telegrambot.annotation.MessageHandler;
import io.github.telegrambot.handler.Handler;
import io.github.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.telegrambot.registry.Registry;
import io.github.telegrambot.model.MessageType;

import java.lang.reflect.Method;

public class MessageAnnotationResolver implements HandlerAnnotationResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(MessageHandler.class);
    }

    @Override
    public void register(String botName, Method method, Handler handler, Registry registry) {
        MessageHandler message = method.getAnnotation(MessageHandler.class);

        String key = botName + "/";

        String methodKey = message.value().isEmpty()
                ? null
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
