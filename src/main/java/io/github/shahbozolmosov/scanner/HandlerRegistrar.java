package io.github.shahbozolmosov.scanner;

import io.github.shahbozolmosov.annotation.BotHandler;
import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.annotation.Updates;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.HandlerMapping;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.scanner.resolver.AnnotationHandlerResolver;
import io.github.shahbozolmosov.type.MessageType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public final class HandlerRegistrar {
    private final ClassScanner scanner;
    private final ClassInstanceFactory factory;
    private final Registry registry;
    private final List<AnnotationHandlerResolver> resolvers;

    public HandlerRegistrar(
            ClassScanner scanner,
            ClassInstanceFactory factory,
            Registry registry,
            List<AnnotationHandlerResolver> resolvers
    ) {
        this.scanner = scanner;
        this.factory = factory;
        this.registry = registry;
        this.resolvers = resolvers;
    }

    public void register(String packageName) {
        List<Class<?>> classes = scanner.scan(packageName, BotHandler.class);

        for (Class<?> clazz : classes) {
            Object instance = factory.create(clazz);

            for (Method method : clazz.getDeclaredMethods()) {
                for (AnnotationHandlerResolver resolver : resolvers) {
                    if (!resolver.supports(method)) {
                        continue;
                    }

                    Handler handler = context -> {
                        try {
                            method.invoke(instance, context);
                        } catch (InvocationTargetException ex) {
                            Throwable cause = ex.getCause();
                            System.err.println("Handler execution failed for update: " + cause);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    };

                    resolver.register(method, handler, registry);
                }
            }
        }
    }
}
