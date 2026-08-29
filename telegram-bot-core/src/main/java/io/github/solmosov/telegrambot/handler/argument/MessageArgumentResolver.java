package io.github.solmosov.telegrambot.handler.argument;

import io.github.solmosov.telegrambot.context.BotContext;
import io.github.solmosov.telegrambot.exception.TelegramBotException;
import io.github.solmosov.telegrambot.model.Message;
import io.github.solmosov.telegrambot.model.Update;

import java.lang.reflect.Parameter;

public class MessageArgumentResolver implements HandlerArgumentResolver {
    @Override
    public boolean supports(Parameter parameter) {
        return parameter.getType() == Message.class;
    }

    @Override
    public Object resolve(
            Parameter parameter,
            Update update,
            BotContext context
    ) {
        if (update.message() == null) {
            throw new TelegramBotException(
                    "Message is not available in current update"
            );
        }

        return update.message();
    }
}
