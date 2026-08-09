package io.github.shahbozolmosov.dispatcher;

import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.type.MessageType;
import io.github.shahbozolmosov.type.UpdateType;

import java.util.List;

public class MessageUpdateDispatcher implements UpdateTypeDispatcher {
    private final Registry registry;

    public MessageUpdateDispatcher(Registry registry) {
        this.registry = registry;
    }

    @Override
    public boolean supports(UpdateType type) {
        return type == UpdateType.MESSAGE;
    }

    @Override
    public void dispatch(Update update, BotContext botContext) {
        Message message = update.message();

        MessageType type = resolveType(message);
        String key = message.text();

        List<Handler> handlers = registry.find(type, key);

        for (Handler handler : handlers) {
            handler.handle(botContext);
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
