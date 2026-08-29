package io.github.solmosov.telegrambot.dispatcher;

import io.github.solmosov.telegrambot.context.BotContext;
import io.github.solmosov.telegrambot.model.Update;
import io.github.solmosov.telegrambot.model.UpdateType;

public interface UpdateTypeDispatcher {

    boolean supports(UpdateType type);

    void dispatch(String botName, Update update, BotContext botContext);
}
