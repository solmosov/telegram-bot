package io.github.telegrambot.dispatcher;

import io.github.telegrambot.authorization.AuthorizationDecision;
import io.github.telegrambot.authorization.AuthorizationManager;
import io.github.telegrambot.context.BotContext;
import io.github.telegrambot.exception.authorization.AccessDeniedException;
import io.github.telegrambot.handler.Handler;
import io.github.telegrambot.model.Update;
import io.github.telegrambot.registry.Registry;

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
        dispatchUpdateHandlers(context);

        for (UpdateTypeDispatcher typeDispatcher : updateTypeDispatchers) {
            if (typeDispatcher.supports(update.type())) {
                typeDispatcher.dispatch(botName, update, context);
            }
        }
    }

    private void dispatchUpdateHandlers(BotContext botContext) {
        List<Handler> handlers = registry.getUpdateHandlers(botName);

        for (Handler handler : handlers) {
            AuthorizationDecision decision = authorizationManager.authorize(botContext, handler);
            if (!decision.isGranted()) {
                throw new AccessDeniedException();
            }
            handler.handle(botContext);
        }
    }
}
