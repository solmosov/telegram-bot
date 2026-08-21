package io.github.shahbozolmosov.telegrambot.dispatcher;

import io.github.shahbozolmosov.telegrambot.context.BotContext;
import io.github.shahbozolmosov.telegrambot.model.Update;
import io.github.shahbozolmosov.telegrambot.model.UpdateType;

public interface UpdateTypeDispatcher {

    boolean supports(UpdateType type);

    void dispatch(String botName, Update update, BotContext botContext);
}
