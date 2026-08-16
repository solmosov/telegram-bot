package io.github.shahbozolmosov.scanner.resolver;

import io.github.shahbozolmosov.annotation.CommandHandler;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.registration.MessageHandlerRegistration;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.model.MessageType;

import java.lang.reflect.Method;

public class CommandAnnotationHandlerResolver implements AnnotationHandlerResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(CommandHandler.class);
    }

    @Override
    public void register(Method method, Handler handler, Registry registry) {
        CommandHandler command = method.getAnnotation(CommandHandler.class);

        MessageHandlerRegistration registration = new MessageHandlerRegistration(
                MessageType.COMMAND,
                command.value(),
                handler
        );

        registry.register(registration);
    }
}
