package io.github.shahbozolmosov.scanner.resolver;

import io.github.shahbozolmosov.annotation.CallbackQuery;
import io.github.shahbozolmosov.callback.CallbackParamResolver;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.Registry;

import java.lang.reflect.Method;

public class CallbackAnnotationHandlerResolver implements AnnotationHandlerResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(CallbackQuery.class);
    }

    @Override
    public void register(Method method, Handler handler, Registry registry) {
        CallbackQuery callbackQuery = method.getAnnotation(CallbackQuery.class);

        String key = callbackQuery.value().isEmpty()
                ? null
                : callbackQuery.value();

        registry.registerCallbackQuery(key, handler);
    }
}
