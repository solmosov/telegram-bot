package io.github.shahbozolmosov.scanner.resolver.imp;

import io.github.shahbozolmosov.annotation.LocationHandler;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.registration.MessageHandlerRegistration;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.model.MessageType;
import io.github.shahbozolmosov.scanner.resolver.AnnotationHandlerResolver;

import java.lang.reflect.Method;

public class LocationHandlerAnnotationHandlerResolver implements AnnotationHandlerResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(LocationHandler.class);
    }

    @Override
    public void register(Method method, Handler handler, Registry registry) {

        LocationHandler location = method.getAnnotation(LocationHandler.class);

        MessageHandlerRegistration registration = new MessageHandlerRegistration(
                MessageType.LOCATION,
                location.value(),
                handler
        );

        registry.register(registration);
    }
}
