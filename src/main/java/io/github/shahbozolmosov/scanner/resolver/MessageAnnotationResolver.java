package io.github.shahbozolmosov.scanner.resolver;

import io.github.shahbozolmosov.annotation.MessageHandler;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.registration.MessageHandlerRegistration;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.model.MessageType;

import java.lang.reflect.Method;

public class MessageAnnotationResolver implements HandlerAnnotationResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(MessageHandler.class);
    }

    @Override
    public void register(String botName, Method method, Handler handler, Registry registry) {
        MessageHandler message = method.getAnnotation(MessageHandler.class);

        String key = botName + "/";

        String methodKey = message.value().isEmpty()
                ? null
                : message.value();

        key += methodKey;

        MessageHandlerRegistration registration = new MessageHandlerRegistration(
                MessageType.TEXT,
                key,
                handler
        );

        registry.register(registration);
    }
}
