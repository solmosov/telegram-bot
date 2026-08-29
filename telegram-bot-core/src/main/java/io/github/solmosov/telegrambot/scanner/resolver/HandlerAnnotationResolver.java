package io.github.solmosov.telegrambot.scanner.resolver;

import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.registry.Registry;

import java.lang.reflect.Method;

public interface HandlerAnnotationResolver {
    boolean supports(Method method);
    void register(String botName, Method method, Handler handler, Registry registry);
}
