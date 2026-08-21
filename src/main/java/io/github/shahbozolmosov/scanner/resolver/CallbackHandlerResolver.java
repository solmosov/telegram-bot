package io.github.shahbozolmosov.scanner.resolver;

import io.github.shahbozolmosov.annotation.CallbackQueryHandler;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.registry.registration.CallbackHandlerRegistration;

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
