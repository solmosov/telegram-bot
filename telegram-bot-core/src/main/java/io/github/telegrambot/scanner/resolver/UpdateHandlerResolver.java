package io.github.telegrambot.scanner.resolver;

import io.github.telegrambot.annotation.UpdatesHandler;
import io.github.telegrambot.handler.Handler;
import io.github.telegrambot.registry.Registry;
import io.github.telegrambot.registry.registration.UpdateHandlerRegistration;

import java.lang.reflect.Method;

public class UpdateHandlerResolver implements HandlerAnnotationResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(UpdatesHandler.class);
    }

    @Override
    public void register(String botName, Method method, Handler handler, Registry registry) {

        UpdateHandlerRegistration registration = new UpdateHandlerRegistration(
                botName,
                handler
        );

        registry.registerUpdateHandler(registration);
    }
}
