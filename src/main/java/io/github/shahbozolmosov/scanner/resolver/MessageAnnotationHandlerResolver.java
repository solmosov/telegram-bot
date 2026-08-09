package io.github.shahbozolmosov.scanner.resolver;

import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.HandlerMapping;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.type.MessageType;

import java.lang.reflect.Method;

public class MessageAnnotationHandlerResolver implements AnnotationHandlerResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(Message.class);
    }

    @Override
    public void register(Method method, Handler handler, Registry registry) {
        Message message = method.getAnnotation(Message.class);

        String key = message.value().isEmpty()
                ? null
                : message.value();

        HandlerMapping registration = new HandlerMapping(
                MessageType.TEXT,
                key,
                handler
        );

        registry.register(registration);
    }
}
