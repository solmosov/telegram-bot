package io.github.telegrambot.authorization;

import io.github.telegrambot.context.BotContext;

public interface AuthorizationProvider {

    AuthorizationPrincipal authenticate(BotContext context);
}
