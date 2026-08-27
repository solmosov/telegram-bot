package io.github.shahbozolmosov.telegrambot.scanner.resolver;

import io.github.shahbozolmosov.telegrambot.annotation.CallbackQueryHandler;
import io.github.shahbozolmosov.telegrambot.handler.Handler;
import io.github.shahbozolmosov.telegrambot.registry.Registry;
import io.github.shahbozolmosov.telegrambot.registry.registration.CallbackHandlerRegistration;

import java.lang.reflect.Method;

public class CallbackHandlerResolver implements HandlerAnnotationResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(CallbackQueryHandler.class);
    }

    @Override
    public void register(String botName, Method method, Handler handler, Registry registry) {
        CallbackQueryHandler callbackQuery = method.getAnnotation(CallbackQueryHandler.class);

        String key = botName;

        String methodKey = callbackQuery.value().isEmpty()
                ? null
                : callbackQuery.value();


        key += methodKey;

        CallbackHandlerRegistration registration = new CallbackHandlerRegistration(
                key,
                handler
        );

        handler.setCallbackPattern(methodKey);
        registry.registerCallbackQuery(registration);
    }
}
