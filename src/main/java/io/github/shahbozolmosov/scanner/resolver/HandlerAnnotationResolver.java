package io.github.shahbozolmosov.scanner.resolver;

import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.Registry;

import java.lang.reflect.Method;

public interface HandlerAnnotationResolver {
    boolean supports(Method method);
    void register(String botName, Method method, Handler handler, Registry registry);
}
