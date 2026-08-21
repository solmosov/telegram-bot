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
        BotAuthorize botAuthorize = handler.authorization();

        if (botAuthorize == null) {
            return AuthorizationDecision.granted();
        }

        if (provider == null) {
            throw new IllegalArgumentException(
                    "AuthorizationProvider is required for handlers using @BotHandler"
            );
        }

        AuthorizationPrincipal principal = provider.authenticate(context);

        if (principal == null) {
            return AuthorizationDecision.denied();
        }

        for (String role : botAuthorize.value()) {
            if (!principal.hasRole(role)) {
                return AuthorizationDecision.denied();
            }
        }

        return AuthorizationDecision.granted();
    }
}
