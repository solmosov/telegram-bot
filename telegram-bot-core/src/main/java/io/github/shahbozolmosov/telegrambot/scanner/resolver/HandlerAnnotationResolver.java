package io.github.shahbozolmosov.telegrambot.scanner.resolver;

import io.github.shahbozolmosov.telegrambot.handler.Handler;
import io.github.shahbozolmosov.telegrambot.registry.Registry;

import java.lang.reflect.Method;

public interface HandlerAnnotationResolver {
    boolean supports(Method method);
    void register(String botName, Method method, Handler handler, Registry registry);
}
