package io.github.telegrambot.dispatcher;

import io.github.telegrambot.context.BotContext;
import io.github.telegrambot.model.Update;
import io.github.telegrambot.model.UpdateType;

public interface UpdateTypeDispatcher {

    boolean supports(UpdateType type);

    void dispatch(String botName, Update update, BotContext botContext);
}
