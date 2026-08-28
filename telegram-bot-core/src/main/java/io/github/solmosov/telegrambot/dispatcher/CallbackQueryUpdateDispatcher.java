package io.github.solmosov.telegrambot.dispatcher;

import io.github.solmosov.telegrambot.authorization.AuthorizationDecision;
import io.github.solmosov.telegrambot.authorization.AuthorizationManager;
import io.github.solmosov.telegrambot.context.BotContext;
import io.github.solmosov.telegrambot.dispatcher.resolver.CallbackParamResolver;
import io.github.solmosov.telegrambot.exception.authorization.AccessDeniedException;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.model.CallbackQuery;
import io.github.solmosov.telegrambot.model.Update;
import io.github.solmosov.telegrambot.model.UpdateType;
import io.github.solmosov.telegrambot.registry.Registry;

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

        String key = botName + callbackQuery.data();

        List<Handler> handlerGroups = registry.findCallbackQuery(key);

        for (Handler handler : handlerGroups) {
            AuthorizationDecision decision = authorizationManager.authorize(botContext, handler);
            if (!decision.isGranted()) {
                throw new AccessDeniedException();
            }

            // Params
            var params = CallbackParamResolver.params(handler.getCallbackPattern(), key);
            botContext.callbackQuery()
                    .setCallbackParams(params);

            // Context
            handler.handle(update, botContext);
        }
    }
}
