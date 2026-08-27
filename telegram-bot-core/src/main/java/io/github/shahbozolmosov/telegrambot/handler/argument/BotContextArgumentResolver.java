package io.github.shahbozolmosov.telegrambot.handler.argument;

import io.github.shahbozolmosov.telegrambot.context.BotContext;
import io.github.shahbozolmosov.telegrambot.model.Update;

import java.lang.reflect.Parameter;

public final class BotContextArgumentResolver implements HandlerArgumentResolver {

    @Override
    public boolean supports(Parameter parameter) {
        return parameter.getType() == BotContext.class;
    }

    @Override
    public Object resolve(Parameter parameter, Update update, BotContext context) {
        return context;
    }
}
