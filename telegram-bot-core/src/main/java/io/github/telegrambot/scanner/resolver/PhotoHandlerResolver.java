package io.github.telegrambot.scanner.resolver;

import io.github.telegrambot.annotation.PhotoHandler;
import io.github.telegrambot.handler.Handler;
import io.github.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.telegrambot.registry.Registry;
import io.github.telegrambot.model.MessageType;

import java.lang.reflect.Method;

public class PhotoHandlerResolver implements HandlerAnnotationResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(PhotoHandler.class);
    }

    @Override
    public void register(String botName, Method method, Handler handler, Registry registry) {

        String key = botName + "/";

        MessageHandlerRegistration registration = new MessageHandlerRegistration(
                MessageType.PHOTO,
                key,
                handler
        );

        registry.register(registration);
    }
}
