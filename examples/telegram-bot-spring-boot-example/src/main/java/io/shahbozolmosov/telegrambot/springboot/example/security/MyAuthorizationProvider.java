package io.shahbozolmosov.telegrambot.springboot.example.security;

import io.github.shahbozolmosov.telegrambot.authorization.AuthorizationPrincipal;
import io.github.shahbozolmosov.telegrambot.authorization.AuthorizationProvider;
import io.github.shahbozolmosov.telegrambot.context.BotContext;
import io.shahbozolmosov.telegrambot.springboot.example.user.MyUser;

import java.util.Map;
import java.util.Set;
public class MyAuthorizationProvider implements AuthorizationProvider {

    // DB
    private final Map<Long, MyUser> users = Map.of(
            5561519648L,
            new MyUser(
                    5561519648L,
                    "admin",
                    Set.of("ADMIN")
            ),

            8975724196L,
            new MyUser(
                    8975724196L,
                    "support",
                    Set.of("SUPPORT")
            )
    );


    @Override
    public AuthorizationPrincipal authenticate(BotContext context) {
        Long telegramUserId = context.message().from().id();

        MyUser user = users.get(telegramUserId);

        if (user == null) {
            return null;
        }

        return new MyAuthorizationPrincipal(user);
    }
}