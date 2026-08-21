package io.github.telegrambot.scanner;

import io.github.telegrambot.annotation.BotAuthorize;
import io.github.telegrambot.annotation.BotHandler;
import io.github.telegrambot.exception.TelegramBotException;
import io.github.telegrambot.handler.Handler;
import io.github.telegrambot.registry.Registry;
import io.github.telegrambot.scanner.resolver.HandlerAnnotationResolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public final class HandlerRegistrar {
    private final ClassScanner scanner;
    private final ClassInstanceFactory factory;
    private final Registry registry;
    private final List<HandlerAnnotationResolver> resolvers;

    public HandlerRegistrar(
            ClassScanner scanner,
            ClassInstanceFactory factory,
            Registry registry,
            List<HandlerAnnotationResolver> resolvers
    ) {
        this.scanner = scanner;
        this.factory = factory;
        this.registry = registry;
        this.resolvers = resolvers;
    }

    public void register(String packageName) {
        List<Class<?>> classes = scanner.scan(packageName, BotHandler.class);

        for (Class<?> clazz : classes) {

            BotHandler botHandler = clazz.getAnnotation(BotHandler.class);
            final String botName = botHandler.value();

            if (botName == null || botName.isBlank()) {
                throw new TelegramBotException("@BotHandler bot name is required");
            }

            Object instance = factory.create(clazz);

            for (Method method : clazz.getDeclaredMethods()) {
                for (HandlerAnnotationResolver resolver : resolvers) {
                    if (!resolver.supports(method)) {
                        continue;
                    }

                    Handler handler = getHandler(method, instance);

                    resolver.register(botName, method, handler, registry);
                }
            }
        }
    }

    private static Handler getHandler(Method method, Object instance) {
        BotAuthorize authorization = method.getAnnotation(BotAuthorize.class);

        return new Handler(
                context -> {
                    try {
                        method.invoke(instance, context);
                    } catch (InvocationTargetException ex) {
                        Throwable cause = ex.getCause();
                        System.err.println("Handler execution failed for update: " + cause);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                },
                authorization
        );
    }
}
