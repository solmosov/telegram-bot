package io.github.shahbozolmosov.dispatcher;

import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.dispatcher.resolver.FallbackMessageTypeResolver;
import io.github.shahbozolmosov.dispatcher.resolver.MessageTypeResolver;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.model.Message;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.type.MessageType;
import io.github.shahbozolmosov.type.UpdateType;

import java.util.List;
import java.util.Optional;

public class MessageUpdateDispatcher implements UpdateTypeDispatcher {

    private final Registry registry;
    private final List<MessageTypeResolver> resolvers;
    private final FallbackMessageTypeResolver fallbackMessageTypeResolver;

    public MessageUpdateDispatcher(
            Registry registry,
            List<MessageTypeResolver> resolvers,
            FallbackMessageTypeResolver fallbackResolver
    ) {
        this.registry = registry;
        this.resolvers = resolvers;
        this.fallbackMessageTypeResolver = fallbackResolver;
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
        for (MessageTypeResolver resolver : resolvers) {
            Optional<MessageType> resolved = resolver.resolve(message);

            if (resolved.isPresent()) {
                return resolved.get();
            }
        }

        return fallbackMessageTypeResolver.resolve(message);
    }
}
