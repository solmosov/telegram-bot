package io.github.shahbozolmosov.telegrambot.scanner.resolver;

import io.github.shahbozolmosov.telegrambot.annotation.PhotoHandler;
import io.github.shahbozolmosov.telegrambot.handler.Handler;
import io.github.shahbozolmosov.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.shahbozolmosov.telegrambot.registry.Registry;
import io.github.shahbozolmosov.telegrambot.model.MessageType;

import java.lang.reflect.Method;

public class PhotoHandlerResolver implements HandlerAnnotationResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(PhotoHandler.class);
    }

    @Override
    public void register(String botName, Method method, Handler handler, Registry registry) {

        String key = botName;

        MessageHandlerRegistration registration = new MessageHandlerRegistration(
                MessageType.PHOTO,
                key,
                handler
        );

        registry.register(registration);
    }
}
