package io.github.shahbozolmosov.telegrambot.handler.argument;

import io.github.shahbozolmosov.telegrambot.context.BotContext;
import io.github.shahbozolmosov.telegrambot.exception.TelegramBotException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public final class HandlerArgumentResolverComposite {

    private final List<HandlerArgumentResolver> resolvers;

    public HandlerArgumentResolverComposite(
            List<HandlerArgumentResolver> resolvers
    ) {
        this.resolvers = resolvers;
    }

    public Object[] resolve(Method method, BotContext context) {
        return Arrays.stream(method.getParameters())
                .map(param -> resolve(param, context))
                .toArray();
    }

    private Object resolve(
            Parameter param,
            BotContext context
    ) {
        return resolvers.stream()
                .filter(resolver -> resolver.supports(param))
                .findFirst()
                .orElseThrow(() -> new TelegramBotException(
                        "Unsupported handler parameter: "
                                + param.getType().getName()
                                + " in method: "
                                + param.getClass().getDeclaringClass().getName()
                                + "#"
                                + param.getName()
                ))
                .resolve(param, context);
    }
}
