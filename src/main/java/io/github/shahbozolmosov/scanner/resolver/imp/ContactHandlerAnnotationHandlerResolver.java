package io.github.shahbozolmosov.scanner.resolver.imp;

import io.github.shahbozolmosov.annotation.ContactHandler;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.registration.MessageHandlerRegistration;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.model.MessageType;
import io.github.shahbozolmosov.scanner.resolver.AnnotationHandlerResolver;

import java.lang.reflect.Method;

public class ContactHandlerAnnotationHandlerResolver implements AnnotationHandlerResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(ContactHandler.class);
    }

    @Override
    public void register(Method method, Handler handler, Registry registry) {
        ContactHandler contactHandler = method.getAnnotation(ContactHandler.class);

        MessageHandlerRegistration registration = new MessageHandlerRegistration(
                MessageType.CONTACT,
                null,
                handler
        );

        registry.register(registration);
    }
}
