package io.github.shahbozolmosov.telegrambot.scanner.resolver;

import io.github.shahbozolmosov.telegrambot.annotation.ContactHandler;
import io.github.shahbozolmosov.telegrambot.handler.Handler;
import io.github.shahbozolmosov.telegrambot.registry.registration.MessageHandlerRegistration;
import io.github.shahbozolmosov.telegrambot.registry.Registry;
import io.github.shahbozolmosov.telegrambot.model.MessageType;

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
