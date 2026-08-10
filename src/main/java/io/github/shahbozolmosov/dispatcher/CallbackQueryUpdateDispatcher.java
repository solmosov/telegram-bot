package io.github.shahbozolmosov.dispatcher;

import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.model.CallbackQuery;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.type.UpdateType;

import java.util.List;

public class CallbackQueryUpdateDispatcher implements UpdateTypeDispatcher {

    private final Registry registry;

    public CallbackQueryUpdateDispatcher(Registry registry) {
        this.registry = registry;
    }

    @Override
    public boolean supports(UpdateType type) {
        return type == UpdateType.CALLBACK_QUERY;
    }

    @Override
    public void dispatch(Update update, BotContext botContext) {
        CallbackQuery callbackQuery = update.callbackQuery();

        List<Handler> handlers = registry.findCallbackQuery(callbackQuery.data());

        for (Handler handler : handlers) {
            handler.handle(botContext);
        }
    }
}
