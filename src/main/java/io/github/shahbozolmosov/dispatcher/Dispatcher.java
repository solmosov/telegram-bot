package io.github.shahbozolmosov.dispatcher;

import io.github.shahbozolmosov.authorization.AuthorizationManager;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.registry.Registry;

import java.util.List;

public final class Dispatcher {

    private final Registry registry;
    private final List<UpdateTypeDispatcher> updateTypeDispatchers;
    private final AuthorizationManager authorizationManager;

    public Dispatcher(
            Registry registry,
            List<UpdateTypeDispatcher> updateTypeDispatchers,
            AuthorizationManager authorizationManager
    ) {
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
                typeDispatcher.dispatch(update, context);
            }
        }
    }

    private void dispatchUpdateHandlers(BotContext botContext) {
        List<Handler> handlers = registry.getUpdateHandlers();

        for (Handler handler : handlers) {
            if (!authorizationManager.authorize(botContext, handler).isGranted()) {
                continue;
            }
            handler.handle(botContext);
        }
    }
}
