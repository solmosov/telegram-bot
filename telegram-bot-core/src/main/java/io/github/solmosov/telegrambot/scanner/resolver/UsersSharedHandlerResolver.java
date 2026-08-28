package io.github.solmosov.telegrambot.scanner.resolver;

import io.github.solmosov.telegrambot.annotation.UsersSharedHandler;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.solmosov.telegrambot.registry.Registry;
import io.github.solmosov.telegrambot.model.MessageType;

import java.lang.reflect.Method;

public class UsersSharedHandlerResolver implements HandlerAnnotationResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(UsersSharedHandler.class);
    }

    @Override
    public void register(String botName, Method method, Handler handler, Registry registry) {
        UsersSharedHandler requestUsersHandler = method.getAnnotation(UsersSharedHandler.class);

        String key = botName;

        String methodKey = requestUsersHandler.value() == -1
                ? ""
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
