package io.github.solmosov.telegrambot.handler.argument;

import io.github.solmosov.telegrambot.context.BotContext;
import io.github.solmosov.telegrambot.model.Update;

import java.lang.reflect.Parameter;

public interface HandlerArgumentResolver {

    boolean supports(Parameter parameter);

    Object resolve(Parameter parameter, Update update, BotContext context);
}
