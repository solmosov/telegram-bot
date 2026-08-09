package io.github.shahbozolmosov.dispatcher;

import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.registry.Registry;

import java.util.List;

public final class Dispatcher {

    private final Registry registry;
    private final List<UpdateTypeDispatcher> updateTypeDispatchers;

    public Dispatcher(
            Registry registry,
            List<UpdateTypeDispatcher> updateTypeDispatchers
            ) {
        this.registry = registry;
        this.updateTypeDispatchers = updateTypeDispatchers;
    }

    public void dispatch(
            Update update,
            BotContext context
    ) {
        dispatchUpdateHandlers(context);

        for(UpdateTypeDispatcher typeDispatcher : updateTypeDispatchers){
            if(typeDispatcher.supports(update.type())){
               typeDispatcher.dispatch(update, context);
            }
        }
    }

    private void dispatchUpdateHandlers(BotContext context) {
        List<Handler> handlers = registry.getUpdateHandlers();

        for (Handler handler : handlers) {
            handler.handle(context);
        }
    }
}
