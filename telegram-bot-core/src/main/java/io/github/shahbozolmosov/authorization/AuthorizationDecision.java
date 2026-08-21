package io.github.shahbozolmosov.authorization;

public record AuthorizationDecision(
        boolean isGranted
) {
    public static AuthorizationDecision granted() {
        return new AuthorizationDecision(true);
    }

    public static AuthorizationDecision denied() {
        return new AuthorizationDecision(false);
    }

}
