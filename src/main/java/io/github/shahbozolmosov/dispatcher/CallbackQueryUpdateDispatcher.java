package io.github.shahbozolmosov.dispatcher;

import io.github.shahbozolmosov.authorization.AuthorizationDecision;
import io.github.shahbozolmosov.authorization.AuthorizationManager;
import io.github.shahbozolmosov.dispatcher.resolver.CallbackParamResolver;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.exception.AccessDeniedException;
import io.github.shahbozolmosov.model.CallbackQuery;
import io.github.shahbozolmosov.model.Update;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.model.UpdateType;
import io.github.shahbozolmosov.registry.store.CallbackHandlerStore;

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
    public void dispatch(Update update, BotContext botContext) {
        CallbackQuery callbackQuery = update.callbackQuery();

        String key = callbackQuery.data();

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
