package io.github.shahbozolmosov.scanner.resolver.imp;

import io.github.shahbozolmosov.annotation.MessageHandler;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.registration.MessageHandlerRegistration;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.model.MessageType;
import io.github.shahbozolmosov.scanner.resolver.AnnotationHandlerResolver;

import java.lang.reflect.Method;

public class MessageAnnotationHandlerResolver implements AnnotationHandlerResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(MessageHandler.class);
    }

    @Override
    public void register(Method method, Handler handler, Registry registry) {
        MessageHandler message = method.getAnnotation(MessageHandler.class);

        String key = message.value().isEmpty()
                ? null
                : message.value();

        MessageHandlerRegistration registration = new MessageHandlerRegistration(
                MessageType.TEXT,
                key,
                handler
        );

        registry.register(registration);
    }
}
