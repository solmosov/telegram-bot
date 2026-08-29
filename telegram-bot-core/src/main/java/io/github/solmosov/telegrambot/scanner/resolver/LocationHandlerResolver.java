package io.github.solmosov.telegrambot.scanner.resolver;

import io.github.solmosov.telegrambot.annotation.LocationHandler;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.solmosov.telegrambot.registry.Registry;
import io.github.solmosov.telegrambot.model.MessageType;

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
                ? ""
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
