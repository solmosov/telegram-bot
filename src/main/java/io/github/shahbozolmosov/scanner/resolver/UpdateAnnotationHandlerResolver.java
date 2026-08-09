package io.github.shahbozolmosov.scanner.resolver;

import io.github.shahbozolmosov.annotation.Updates;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.Registry;

import java.lang.reflect.Method;

public class UpdateAnnotationHandlerResolver implements AnnotationHandlerResolver{
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(Updates.class);
    }

    @Override
    public void register(Method method, Handler handler, Registry registry) {
        registry.registerUpdateHandler(handler);
    }
}
