package io.github.shahbozolmosov.example.authorization;

import io.github.shahbozolmosov.authorization.AuthorizationPrincipal;
import io.github.shahbozolmosov.example.user.MyUser;

import java.util.Set;

public class MyAuthorizationPrincipal implements AuthorizationPrincipal {

    private final MyUser user;

    public MyAuthorizationPrincipal(MyUser user) {
        this.user = user;
    }

    @Override
    public Object getUser() {
        return user;
    }

    @Override
    public Set<String> getRoles() {
        return user.roles();
    }
}
