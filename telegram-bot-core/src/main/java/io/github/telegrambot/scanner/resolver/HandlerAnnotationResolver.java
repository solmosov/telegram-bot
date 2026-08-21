package io.github.telegrambot.scanner.resolver;

import io.github.telegrambot.handler.Handler;
import io.github.telegrambot.registry.Registry;

import java.lang.reflect.Method;

public interface HandlerAnnotationResolver {
    boolean supports(Method method);
    void register(String botName, Method method, Handler handler, Registry registry);
}
