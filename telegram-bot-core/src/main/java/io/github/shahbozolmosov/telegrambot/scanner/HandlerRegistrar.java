package io.github.shahbozolmosov.telegrambot.scanner;

import io.github.shahbozolmosov.telegrambot.annotation.BotAuthorize;
import io.github.shahbozolmosov.telegrambot.annotation.BotHandler;
import io.github.shahbozolmosov.telegrambot.exception.TelegramBotException;
import io.github.shahbozolmosov.telegrambot.handler.Handler;
import io.github.shahbozolmosov.telegrambot.handler.argument.HandlerArgumentResolverComposite;
import io.github.shahbozolmosov.telegrambot.registry.Registry;
import io.github.shahbozolmosov.telegrambot.scanner.resolver.HandlerAnnotationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public final class HandlerRegistrar {
    private final static Logger log = LoggerFactory.getLogger(HandlerRegistrar.class);

    private final ClassScanner scanner;
    private final ClassInstanceFactory factory;
    private final Registry registry;
    private final List<HandlerAnnotationResolver> resolvers;
    private final HandlerArgumentResolverComposite argumentResolver;

    private final String botName;


    public HandlerRegistrar(
            ClassScanner scanner,
            ClassInstanceFactory factory,
            Registry registry,
            List<HandlerAnnotationResolver> resolvers,
            HandlerArgumentResolverComposite argumentResolver,
            String botName
    ) {
        this.scanner = scanner;
        this.factory = factory;
        this.registry = registry;
        this.resolvers = resolvers;
        this.argumentResolver = argumentResolver;
        this.botName = botName;
    }

    public void register(String packageName) {
        List<Class<?>> classes = scanner.scan(packageName, BotHandler.class);

        for (Class<?> clazz : classes) {
            register(factory.create(clazz));
        }
    }

    public void register(Object instance) {
        Class<?> clazz = instance.getClass();

        BotHandler botHandler = clazz.getAnnotation(BotHandler.class);

        if (botHandler == null) {
            throw new TelegramBotException("Handler class must be annotated with @BotHandler");
        }

        final String botNameOfHandler = botHandler.value();
        if (botNameOfHandler == null || botNameOfHandler.isBlank()) {
            throw new TelegramBotException("@BotHandler bot name is required");
        }

        if (!botName.equals(botNameOfHandler)) {
            return;
        }

        for (Method method : clazz.getDeclaredMethods()) {
            for (HandlerAnnotationResolver resolver : resolvers) {
                if (!resolver.supports(method)) {
                    continue;
                }

                Handler handler = getHandler(method, instance);

                resolver.register(botNameOfHandler, method, handler, registry);
            }
        }
    }


    private Handler getHandler(Method method, Object instance) {
        BotAuthorize authorization = method.getAnnotation(BotAuthorize.class);

        return new Handler(
                context -> {
                    try {
                        Object[] arguments = argumentResolver.resolve(method, context);

                        method.invoke(instance, arguments);

                    } catch (InvocationTargetException ex) {
                        Throwable cause = ex.getCause();
                        log.error("Handler execution failed for update", cause);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                },
                authorization
        );
    }
}
