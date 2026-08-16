package io.github.shahbozolmosov.scanner.resolver;

import io.github.shahbozolmosov.annotation.CallbackQueryHandler;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.registry.registration.CallbackHandlerRegistration;

import java.lang.reflect.Method;

public class CallbackAnnotationHandlerResolver implements AnnotationHandlerResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(CallbackQueryHandler.class);
    }

    @Override
    public void register(Method method, Handler handler, Registry registry) {
        CallbackQueryHandler callbackQuery = method.getAnnotation(CallbackQueryHandler.class);

        String key = callbackQuery.value().isEmpty()
                ? null
                : callbackQuery.value();

        CallbackHandlerRegistration registration = new CallbackHandlerRegistration(
                key,
                handler
        );

        registry.registerCallbackQuery(registration);
    }
}
