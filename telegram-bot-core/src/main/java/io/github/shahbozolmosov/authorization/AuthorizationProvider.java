package io.github.shahbozolmosov.authorization;

import io.github.shahbozolmosov.context.BotContext;

public interface AuthorizationProvider {

    AuthorizationPrincipal authenticate(BotContext context);
}
