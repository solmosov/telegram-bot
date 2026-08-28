package io.github.solmosov.telegrambot.authorization;

import io.github.solmosov.telegrambot.context.BotContext;

public interface AuthorizationProvider {

    AuthorizationPrincipal authenticate(BotContext context);
}
