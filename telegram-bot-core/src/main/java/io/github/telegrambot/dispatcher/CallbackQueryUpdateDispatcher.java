package io.github.telegrambot.dispatcher;

import io.github.telegrambot.authorization.AuthorizationDecision;
import io.github.telegrambot.authorization.AuthorizationManager;
import io.github.telegrambot.dispatcher.resolver.CallbackParamResolver;
import io.github.telegrambot.context.BotContext;
import io.github.telegrambot.exception.authorization.AccessDeniedException;
import io.github.telegrambot.model.CallbackQuery;
import io.github.telegrambot.model.Update;
import io.github.telegrambot.registry.Registry;
import io.github.telegrambot.model.UpdateType;
import io.github.telegrambot.registry.store.CallbackHandlerStore;

import java.util.List;

public class CallbackQueryUpdateDispatcher implements UpdateTypeDispatcher {

    private final Registry registry;
    private final AuthorizationManager authorizationManager;

    public CallbackQueryUpdateDispatcher(
            Registry registry,
            AuthorizationManager authorizationManager
    ) {
        this.registry = registry;
        this.authorizationManager = authorizationManager;
    }

    @Override
    public boolean supports(UpdateType type) {
        return type == UpdateType.CALLBACK_QUERY;
    }

    @Override
    public void dispatch(String botName, Update update, BotContext botContext) {
        CallbackQuery callbackQuery = update.callbackQuery();

        String key = botName + "/" + callbackQuery.data();

        List<CallbackHandlerStore.CallbackHandlerGroup> handlerGroups = registry.findCallbackQuery(key);

        for (CallbackHandlerStore.CallbackHandlerGroup group : handlerGroups) {
            AuthorizationDecision decision = authorizationManager.authorize(botContext, group.handler());
            if (!decision.isGranted()) {
                throw new AccessDeniedException();
            }

            var params = CallbackParamResolver.params(group.callbackPattern(), key);
            botContext.setCallbackParams(params);
            group.handler().handle(botContext);
        }
    }
}
