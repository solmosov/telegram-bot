package io.github.shahbozolmosov.scanner.resolver.imp;

import io.github.shahbozolmosov.annotation.PhotoHandler;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.registration.MessageHandlerRegistration;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.model.MessageType;
import io.github.shahbozolmosov.scanner.resolver.AnnotationHandlerResolver;

import java.lang.reflect.Method;

public class PhotoAnnotationHandlerResolver implements AnnotationHandlerResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(PhotoHandler.class);
    }

    @Override
    public void register(Method method, Handler handler, Registry registry) {
        MessageHandlerRegistration registration = new MessageHandlerRegistration(
                MessageType.PHOTO,
                null,
                handler
        );

        registry.register(registration);
    }
}
