package io.github.solmosov.telegrambot.dispatcher;

import io.github.solmosov.telegrambot.authorization.AuthorizationDecision;
import io.github.solmosov.telegrambot.authorization.AuthorizationManager;
import io.github.solmosov.telegrambot.context.BotContext;
import io.github.solmosov.telegrambot.exception.authorization.AccessDeniedException;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.model.Update;
import io.github.solmosov.telegrambot.registry.Registry;

import java.util.List;

public final class Dispatcher {

    private final String botName;
    private final Registry registry;
    private final List<UpdateTypeDispatcher> updateTypeDispatchers;
    private final AuthorizationManager authorizationManager;

    public Dispatcher(
            String botName,
            Registry registry,
            List<UpdateTypeDispatcher> updateTypeDispatchers,
            AuthorizationManager authorizationManager
    ) {
        this.botName = botName;
        this.registry = registry;
        this.updateTypeDispatchers = updateTypeDispatchers;
        this.authorizationManager = authorizationManager;
    }

    public void dispatch(
            Update update,
            BotContext context
    ) {
        dispatchUpdateHandlers(update, context);

        for (UpdateTypeDispatcher typeDispatcher : updateTypeDispatchers) {
            if (typeDispatcher.supports(update.type())) {
                typeDispatcher.dispatch(botName, update, context);
            }
        }
    }

    private void dispatchUpdateHandlers(Update update, BotContext botContext) {
        Handler handler = registry.getUpdateHandler(botName);

        if (handler != null) {
            AuthorizationDecision decision = authorizationManager.authorize(botContext, handler);
            if (!decision.isGranted()) {
                throw new AccessDeniedException();
            }
            handler.handle(update, botContext);
        }
    }
}
