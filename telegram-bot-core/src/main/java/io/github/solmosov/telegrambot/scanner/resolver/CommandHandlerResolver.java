package io.github.solmosov.telegrambot.scanner.resolver;

import io.github.solmosov.telegrambot.annotation.CommandHandler;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.solmosov.telegrambot.registry.Registry;
import io.github.solmosov.telegrambot.model.MessageType;

import java.lang.reflect.Method;

public class CommandHandlerResolver implements HandlerAnnotationResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(CommandHandler.class);
    }

    @Override
    public void register(String botName, Method method, Handler handler, Registry registry) {
        CommandHandler command = method.getAnnotation(CommandHandler.class);

        String methodKey = command.value().isEmpty()
                ? null
                : command.value();

        String key = botName + methodKey;

        MessageHandlerRegistration registration = new MessageHandlerRegistration(
                MessageType.COMMAND,
                key,
                handler
        );

        registry.register(registration);
    }
}
