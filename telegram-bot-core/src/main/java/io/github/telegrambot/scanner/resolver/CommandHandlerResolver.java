package io.github.telegrambot.scanner.resolver;

import io.github.telegrambot.annotation.CommandHandler;
import io.github.telegrambot.handler.Handler;
import io.github.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.telegrambot.registry.Registry;
import io.github.telegrambot.model.MessageType;

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

        String key = botName + "/" + methodKey;

        MessageHandlerRegistration registration = new MessageHandlerRegistration(
                MessageType.COMMAND,
                key,
                handler
        );

        registry.register(registration);
    }
}
