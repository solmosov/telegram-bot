package io.github.solmosov.telegrambot.scanner.resolver;

import io.github.solmosov.telegrambot.annotation.ContactHandler;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.solmosov.telegrambot.registry.Registry;
import io.github.solmosov.telegrambot.model.MessageType;

import java.lang.reflect.Method;

public class ContactHandlerResolver implements HandlerAnnotationResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(ContactHandler.class);
    }

    @Override
    public void register(String botName, Method method, Handler handler, Registry registry) {

        String key = botName;

        MessageHandlerRegistration registration = new MessageHandlerRegistration(
                MessageType.CONTACT,
                key,
                handler
        );

        registry.register(registration);
    }
}
