package io.github.shahbozolmosov.example.authorization;

import io.github.shahbozolmosov.authorization.AuthorizationPrincipal;
import io.github.shahbozolmosov.authorization.AuthorizationProvider;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.example.user.MyUser;

import java.util.Map;
import java.util.Set;

public class MyAuthorizationProvider implements AuthorizationProvider {

    // DB
    private final Map<Long, MyUser> users = Map.of(
            8975724196L,
            new MyUser(
                    8975724196L,
                    "admin",
                    Set.of("ADMIN")
            ),

            987654321L,
            new MyUser(
                    987654321L,
                    "courier",
                    Set.of("COURIER")
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
