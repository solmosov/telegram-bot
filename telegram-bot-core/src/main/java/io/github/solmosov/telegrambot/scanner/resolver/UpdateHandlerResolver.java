package io.github.solmosov.telegrambot.scanner.resolver;

import io.github.solmosov.telegrambot.annotation.UpdatesHandler;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.registry.Registry;
import io.github.solmosov.telegrambot.registry.registration.UpdateHandlerRegistration;

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
