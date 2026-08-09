package io.github.shahbozolmosov.scanner.resolver;

import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.HandlerMapping;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.type.MessageType;

import java.lang.reflect.Method;

public class CommandAnnotationHandlerResolver implements AnnotationHandlerResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(Command.class);
    }

    @Override
    public void register(Method method, Handler handler, Registry registry) {
        Command command = method.getAnnotation(Command.class);

        HandlerMapping registration = new HandlerMapping(
                MessageType.COMMAND,
                command.value(),
                handler
        );

        registry.register(registration);
    }
}
