package io.github.shahbozolmosov.dispatcher;

import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.type.MessageType;
import io.github.shahbozolmosov.type.UpdateType;

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
            if(update.type() == UpdateType.MESSAGE){
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

    private MessageType resolveType(Message message) {
        String text = message.text();

        if (text != null && text.startsWith("/")) {
            return MessageType.COMMAND;
        }

        return MessageType.TEXT;
    }
}
