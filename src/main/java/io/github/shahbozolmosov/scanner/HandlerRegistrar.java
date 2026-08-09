package io.github.shahbozolmosov.scanner;

import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.annotation.Updates;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.HandlerMapping;
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

            boolean hasHandler = false;

            for (Method method : methods) {
                if (method.isAnnotationPresent(Command.class) || method.isAnnotationPresent(Message.class)) {
                    hasHandler = true;
                    break;
                }
            }

            if (!hasHandler) {
                continue;
            }

            Object instance = factory.create(clazz);

            for (Method method : methods) {

                Command command = method.getAnnotation(Command.class);
                Message message = method.getAnnotation(Message.class);
                Updates updates = method.getAnnotation(Updates.class);

                if (command == null && message == null && updates == null) {
                    continue;
                }

                Handler handler = context -> {
                    try {
                        method.invoke(instance, context);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                };

                // @Command
                if (command != null) {
                    HandlerMapping registration = new HandlerMapping(
                            MessageType.COMMAND,
                            command.value(),
                            handler
                    );
                    registry.register(registration);
                }

                // @Message
                if (message != null) {
                    String key = message.value().isEmpty()
                            ? null
                            : message.value();

                    HandlerMapping registration = new HandlerMapping(
                            MessageType.TEXT,
                            key,
                            handler
                    );
                    registry.register(registration);
                }

                // @Update
                if (updates != null) {
                    registry.registerUpdateHandler(handler);
                }
            }
        }
    }
}
