package io.github.shahbozolmosov.telegrambot.scanner.resolver;

import io.github.shahbozolmosov.telegrambot.annotation.LocationHandler;
import io.github.shahbozolmosov.telegrambot.handler.Handler;
import io.github.shahbozolmosov.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.shahbozolmosov.telegrambot.registry.Registry;
import io.github.shahbozolmosov.telegrambot.model.MessageType;

import java.lang.reflect.Method;

public class LocationHandlerResolver implements HandlerAnnotationResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(LocationHandler.class);
    }

    @Override
    public void register(String botName, Method method, Handler handler, Registry registry) {

        LocationHandler location = method.getAnnotation(LocationHandler.class);

        String key = botName;

        String methodKey = location.value().isEmpty()
                ? null
                : location.value();


        key += methodKey;

        MessageHandlerRegistration registration = new MessageHandlerRegistration(
                MessageType.LOCATION,
                key,
                handler
        );

        registry.register(registration);
    }

    private static StringBuilder getKey(StringBuilder key) {
        return key;
    }
}
