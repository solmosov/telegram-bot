package io.github.shahbozolmosov.telegrambot.handler.argument;

import io.github.shahbozolmosov.telegrambot.context.BotContext;

import java.lang.reflect.Parameter;

public interface HandlerArgumentResolver {

    boolean supports(Parameter parameter);

    Object resolve(Parameter parameter, BotContext context);
}
