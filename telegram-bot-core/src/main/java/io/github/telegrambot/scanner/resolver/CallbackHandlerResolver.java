package io.github.telegrambot.scanner.resolver;

import io.github.telegrambot.annotation.CallbackQueryHandler;
import io.github.telegrambot.handler.Handler;
import io.github.telegrambot.registry.Registry;
import io.github.telegrambot.registry.registration.CallbackHandlerRegistration;

import java.lang.reflect.Method;

public class CallbackHandlerResolver implements HandlerAnnotationResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(CallbackQueryHandler.class);
    }

    @Override
    public void register(String botName, Method method, Handler handler, Registry registry) {
        CallbackQueryHandler callbackQuery = method.getAnnotation(CallbackQueryHandler.class);

        String key = botName + "/";

        String methodKey = callbackQuery.value().isEmpty()
                ? null
                : callbackQuery.value();


        key += methodKey;

        CallbackHandlerRegistration registration = new CallbackHandlerRegistration(
                key,
                handler
        );

        registry.registerCallbackQuery(registration);
    }
}
