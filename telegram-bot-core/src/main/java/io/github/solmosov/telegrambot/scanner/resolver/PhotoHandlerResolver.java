package io.github.solmosov.telegrambot.scanner.resolver;

import io.github.solmosov.telegrambot.annotation.PhotoHandler;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.solmosov.telegrambot.registry.Registry;
import io.github.solmosov.telegrambot.model.MessageType;

import java.lang.reflect.Method;

public class PhotoHandlerResolver implements HandlerAnnotationResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(PhotoHandler.class);
    }

    @Override
    public void register(String botName, Method method, Handler handler, Registry registry) {

        MessageHandlerRegistration registration = new MessageHandlerRegistration(
                MessageType.PHOTO,
                botName, // key
                handler
        );

        registry.register(registration);
    }
}
