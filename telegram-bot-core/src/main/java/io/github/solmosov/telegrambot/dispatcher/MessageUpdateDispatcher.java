package io.github.solmosov.telegrambot.dispatcher;

import io.github.solmosov.telegrambot.authorization.AuthorizationDecision;
import io.github.solmosov.telegrambot.authorization.AuthorizationManager;
import io.github.solmosov.telegrambot.context.BotContext;
import io.github.solmosov.telegrambot.dispatcher.resolver.DeepLinkParamResolver;
import io.github.solmosov.telegrambot.dispatcher.resolver.FallbackMessageTypeResolver;
import io.github.solmosov.telegrambot.dispatcher.resolver.MessageTypeResolver;
import io.github.solmosov.telegrambot.exception.authorization.AccessDeniedException;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.model.Message;
import io.github.solmosov.telegrambot.model.Update;
import io.github.solmosov.telegrambot.registry.Registry;
import io.github.solmosov.telegrambot.model.MessageType;
import io.github.solmosov.telegrambot.model.UpdateType;

import java.util.List;
import java.util.Optional;

public class MessageUpdateDispatcher implements UpdateTypeDispatcher {

    private final Registry registry;
    private final List<MessageTypeResolver> resolvers;
    private final FallbackMessageTypeResolver fallbackMessageTypeResolver;
    private final AuthorizationManager authorizationManager;

    public MessageUpdateDispatcher(
            Registry registry,
            List<MessageTypeResolver> resolvers,
            FallbackMessageTypeResolver fallbackResolver,
            AuthorizationManager authorizationManager
    ) {
        this.registry = registry;
        this.resolvers = resolvers;
        this.fallbackMessageTypeResolver = fallbackResolver;
        this.authorizationManager = authorizationManager;
    }

    @Override
    public boolean supports(UpdateType type) {
        return type == UpdateType.MESSAGE;
    }

    @Override
    public void dispatch(String botName, Update update, BotContext botContext) {
        Message message = update.message();

        MessageType type = resolveType(message);
        String key = botName + resolveKey(type, message);

        List<Handler> handlers = registry.find(type, key);

        if (type == MessageType.COMMAND && message.text() != null && message.text().startsWith("/start")) {
            botContext.setDeepLinkParam(DeepLinkParamResolver.param(message.text()));
        }

        for (Handler handler : handlers) {
            AuthorizationDecision decision = authorizationManager.authorize(botContext, handler);
            if (!decision.isGranted()) {
                throw new AccessDeniedException();
            }
            handler.handle(update, botContext);
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

    private String resolveKey(MessageType type, Message message) {
        return switch (type) {
            case COMMAND -> message.text().split("\\s+")[0];
            case PHOTO -> message.caption();
            // Reply Keyboard actions
            case LOCATION -> message.replyToMessage().text();
            case USERS_SHARED -> String.valueOf(message.usersShared().requestId());
            default -> message.text();
        };
    }
}
