package io.github.shahbozolmosov.scanner.resolver.imp;

import io.github.shahbozolmosov.annotation.UpdatesHandler;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.registry.registration.UpdateHandlerRegistration;
import io.github.shahbozolmosov.scanner.resolver.AnnotationHandlerResolver;

import java.lang.reflect.Method;

public class UpdateAnnotationHandlerResolver implements AnnotationHandlerResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(UpdatesHandler.class);
    }

    @Override
    public void register(Method method, Handler handler, Registry registry) {

        UpdateHandlerRegistration registration = new UpdateHandlerRegistration(
                handler
        );

        registry.registerUpdateHandler(registration);
    }
}
