package io.github.shahbozolmosov.scanner.resolver;

import io.github.shahbozolmosov.annotation.PhotoHandler;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.HandlerMapping;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.type.MessageType;

import java.lang.reflect.Method;

public class PhotoAnnotationHandlerResolver implements AnnotationHandlerResolver{
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(PhotoHandler.class);
    }

    @Override
    public void register(Method method, Handler handler, Registry registry) {
        HandlerMapping registration = new HandlerMapping(
                MessageType.PHOTO,
                null,
                handler
        );

        registry.register(registration);
    }
}
