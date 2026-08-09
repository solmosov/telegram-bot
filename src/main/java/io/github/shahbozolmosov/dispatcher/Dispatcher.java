package io.github.shahbozolmosov.dispatcher;

import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.registery.Registry;
import io.github.shahbozolmosov.type.MessageType;

public final class Dispatcher {

    private final Registry registry;

    public Dispatcher(Registry registry) {
        this.registry = registry;
    }

    public void dispatch(
            Update update,
            BotContext context
    ) {
        Message message = update.message();

        if (message == null) {
            return;
        }

        MessageType type = resolveType(message);

        String key = message.text();

        Handler handler = registry.find(type, key);

        if (handler != null) {
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
