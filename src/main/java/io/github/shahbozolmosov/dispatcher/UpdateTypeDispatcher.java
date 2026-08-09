package io.github.shahbozolmosov.dispatcher;

import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.type.UpdateType;

public interface UpdateTypeDispatcher {

    boolean supports(UpdateType type);

    void dispatch(Update update, BotContext botContext);
}
