package io.github.shahbozolmosov.authorization;

import java.util.Set;

public interface AuthorizationPrincipal {

    Object getUser();

    Set<String> getRoles();

    default boolean hasRole(String role) {
        return getRoles().contains(role);
    }
}
