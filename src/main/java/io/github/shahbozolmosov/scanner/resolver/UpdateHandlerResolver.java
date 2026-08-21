package io.github.shahbozolmosov.scanner.resolver;

import io.github.shahbozolmosov.annotation.UpdatesHandler;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.registry.registration.UpdateHandlerRegistration;

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
