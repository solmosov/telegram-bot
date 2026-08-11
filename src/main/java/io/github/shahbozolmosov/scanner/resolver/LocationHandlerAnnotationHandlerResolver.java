package io.github.shahbozolmosov.scanner.resolver;

import io.github.shahbozolmosov.annotation.LocationHandler;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.HandlerMapping;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.type.MessageType;

import java.lang.reflect.Method;

public class LocationHandlerAnnotationHandlerResolver implements AnnotationHandlerResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(LocationHandler.class);
    }

    @Override
    public void register(Method method, Handler handler, Registry registry) {

        LocationHandler location = method.getAnnotation(LocationHandler.class);

        HandlerMapping registration = new HandlerMapping(
                MessageType.LOCATION,
                location.value(),
                handler
        );

        registry.register(registration);
    }
}
