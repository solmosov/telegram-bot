package io.github.shahbozolmosov.scanner;

import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.HandlerRegistration;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.type.MessageType;

import java.lang.reflect.Method;
import java.util.List;

public final class HandlerRegistrar {
    private final ClassScanner scanner;
    private final ClassInstanceFactory factory;
    private final Registry registry;

    public HandlerRegistrar(
            ClassScanner scanner,
            ClassInstanceFactory factory,
            Registry registry
    ) {
        this.scanner = scanner;
        this.factory = factory;
        this.registry = registry;
    }

    public void register(String packageName) {
        List<Class<?>> classes = scanner.scan(packageName);

        for (Class<?> clazz : classes) {

            if (clazz.isInterface()) {
                continue;
            }

            Method[] methods = clazz.getDeclaredMethods();

            boolean hasCommand = false;

            for (Method method : methods) {
                if (method.isAnnotationPresent(Command.class)) {
                    hasCommand = true;
                    break;
                }
            }

            if (!hasCommand) {
                continue;
            }

            Object instance = factory.create(clazz);

            for (Method method : methods) {

                Command command = method.getAnnotation(Command.class);

                if (command == null) {
                    continue;
                }

                Handler handler = context -> {
                    try {
                        method.invoke(instance, context);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                };

                HandlerRegistration registration = new HandlerRegistration(
                        MessageType.COMMAND,
                        command.value(),
                        handler
                );

                registry.register(registration);
            }
        }
    }
}
