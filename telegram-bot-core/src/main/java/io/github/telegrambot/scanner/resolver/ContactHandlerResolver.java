package io.github.telegrambot.scanner.resolver;

import io.github.telegrambot.annotation.ContactHandler;
import io.github.telegrambot.handler.Handler;
import io.github.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.telegrambot.registry.Registry;
import io.github.telegrambot.model.MessageType;

import java.lang.reflect.Method;

public class ContactHandlerResolver implements HandlerAnnotationResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(ContactHandler.class);
    }

    @Override
    public void register(String botName, Method method, Handler handler, Registry registry) {

        String key = botName + "/";

        MessageHandlerRegistration registration = new MessageHandlerRegistration(
                MessageType.CONTACT,
                key,
                handler
        );

        registry.register(registration);
    }
}
