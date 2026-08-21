package io.github.telegrambot.scanner.resolver;

import io.github.telegrambot.annotation.RequestUsersHandler;
import io.github.telegrambot.handler.Handler;
import io.github.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.telegrambot.registry.Registry;
import io.github.telegrambot.model.MessageType;

import java.lang.reflect.Method;

public class RequestUsersHandlerResolver implements HandlerAnnotationResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(RequestUsersHandler.class);
    }

    @Override
    public void register(String botName, Method method, Handler handler, Registry registry) {
        RequestUsersHandler requestUsersHandler = method.getAnnotation(RequestUsersHandler.class);

        String key = botName + "/";

        String methodKey = requestUsersHandler.value() == -1
                ? null
                : String.valueOf(requestUsersHandler.value());

        key += methodKey;

        MessageHandlerRegistration registration = new MessageHandlerRegistration(
                MessageType.USERS_SHARED,
                key,
                handler
        );

        registry.register(registration);
    }
}
