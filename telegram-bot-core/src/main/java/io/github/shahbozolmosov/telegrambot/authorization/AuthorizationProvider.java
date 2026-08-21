package io.github.shahbozolmosov.telegrambot.authorization;

import io.github.shahbozolmosov.telegrambot.context.BotContext;

public interface AuthorizationProvider {

    AuthorizationPrincipal authenticate(BotContext context);
}
