package io.github.shahbozolmosov.authorization;

import io.github.shahbozolmosov.annotation.BotAuthorize;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.handler.Handler;

public class AuthorizationManager {

    private final AuthorizationProvider provider;

    public AuthorizationManager(AuthorizationProvider provider) {
        this.provider = provider;
    }

    public AuthorizationDecision authorize(
            BotContext context,
            Handler handler
    ) {
        BotAuthorize authorization = handler.authorization();

        if (authorization == null) {
            return AuthorizationDecision.granted();
        }

        AuthorizationPrincipal principal = provider.authenticate(context);

        for (String role : authorization.value()) {
            if (!principal.hasRole(role)) {
                return AuthorizationDecision.denied();
            }
        }

        return AuthorizationDecision.granted();
    }
}
