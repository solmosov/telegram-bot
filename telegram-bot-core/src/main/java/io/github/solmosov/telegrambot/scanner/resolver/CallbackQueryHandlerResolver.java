package io.github.solmosov.telegrambot.scanner.resolver;

import io.github.solmosov.telegrambot.annotation.CallbackQueryHandler;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.registry.Registry;
import io.github.solmosov.telegrambot.registry.registration.CallbackQueryHandlerRegistration;

import java.lang.reflect.Method;

public class CallbackQueryHandlerResolver implements HandlerAnnotationResolver {
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

        CallbackQueryHandlerRegistration registration = new CallbackQueryHandlerRegistration(
                key,
                handler
        );

        handler.setCallbackPattern(methodKey);
        registry.registerCallbackQuery(registration);
    }
}
