package io.github.solmosov.telegrambot.authorization;

import io.github.solmosov.telegrambot.annotation.BotAuthorize;
import io.github.solmosov.telegrambot.context.BotContext;
import io.github.solmosov.telegrambot.handler.Handler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizationManagerTest {

    private AuthorizationProvider provider;
    private AuthorizationManager manager;
    private BotContext context;
    private Handler handler;
    private BotAuthorize authorization;

    @BeforeEach
    void setUp() {
        provider = mock(AuthorizationProvider.class);
        manager = new AuthorizationManager(provider);

        context = mock(BotContext.class);
        handler = mock(Handler.class);
        authorization = mock(BotAuthorize.class);
    }

    @Test
    void shouldGrantWhenHandlerHasNoAuthorization() {
        when(handler.authorization()).thenReturn(null);

        AuthorizationDecision decision = manager.authorize(context, handler);

        assertTrue(decision.isGranted());
        verifyNoInteractions(provider);
    }

    @Test
    void shouldThrowWhenAuthorizationProviderIsNull() {
        AuthorizationManager manager = new AuthorizationManager(null);

        when(handler.authorization()).thenReturn(authorization);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> manager.authorize(context, handler)
        );

        assertEquals(
                "AuthorizationProvider is required for handlers using @BotHandler",
                exception.getMessage()
        );
    }

    @Test
    void shouldDenyWhenAuthenticationReturnsNull() {
        when(handler.authorization()).thenReturn(authorization);
        when(provider.authenticate(context)).thenReturn(null);

        AuthorizationDecision decision = manager.authorize(context, handler);

        assertFalse(decision.isGranted());
        verify(provider).authenticate(context);
    }

    @Test
    void shouldGrantWhenPrincipalHasRequiredRole() {
        when(handler.authorization()).thenReturn(authorization);
        when(authorization.value()).thenReturn(new String[]{"ADMIN"});

        AuthorizationPrincipal principal = mock(AuthorizationPrincipal.class);
        when(provider.authenticate(context)).thenReturn(principal);
        when(principal.hasRole("ADMIN")).thenReturn(true);

        AuthorizationDecision decision = manager.authorize(context, handler);

        assertTrue(decision.isGranted());

        verify(provider).authenticate(context);
        verify(principal).hasRole("ADMIN");
    }

    @Test
    void shouldDenyWhenPrincipalDoesNotHaveRequiredRole() {
        when(handler.authorization()).thenReturn(authorization);
        when(authorization.value()).thenReturn(new String[]{"ADMIN"});

        AuthorizationPrincipal principal = mock(AuthorizationPrincipal.class);
        when(provider.authenticate(context)).thenReturn(principal);
        when(principal.hasRole("ADMIN")).thenReturn(false);

        AuthorizationDecision decision = manager.authorize(context, handler);

        assertFalse(decision.isGranted());

        verify(provider).authenticate(context);
        verify(principal).hasRole("ADMIN");
    }

    @Test
    void shouldGrantWhenPrincipalHasAllRequiredRoles() {
        when(handler.authorization()).thenReturn(authorization);
        when(authorization.value()).thenReturn(
                new String[]{"ADMIN", "MODERATOR"}
        );

        AuthorizationPrincipal principal = mock(AuthorizationPrincipal.class);
        when(provider.authenticate(context)).thenReturn(principal);

        when(principal.hasRole("ADMIN")).thenReturn(true);
        when(principal.hasRole("MODERATOR")).thenReturn(true);

        AuthorizationDecision decision = manager.authorize(context, handler);

        assertTrue(decision.isGranted());

        verify(principal).hasRole("ADMIN");
        verify(principal).hasRole("MODERATOR");
    }

    @Test
    void shouldDenyWhenPrincipalMissesOneOfRequiredRoles() {
        when(handler.authorization()).thenReturn(authorization);
        when(authorization.value()).thenReturn(
                new String[]{"ADMIN", "MODERATOR"}
        );

        AuthorizationPrincipal principal = mock(AuthorizationPrincipal.class);
        when(provider.authenticate(context)).thenReturn(principal);

        when(principal.hasRole("ADMIN")).thenReturn(true);
        when(principal.hasRole("MODERATOR")).thenReturn(false);

        AuthorizationDecision decision = manager.authorize(context, handler);

        assertFalse(decision.isGranted());

        verify(principal).hasRole("ADMIN");
        verify(principal).hasRole("MODERATOR");
    }

    @Test
    void shouldNotCheckRemainingRolesAfterFirstMissingRole() {
        when(handler.authorization()).thenReturn(authorization);
        when(authorization.value()).thenReturn(
                new String[]{"ADMIN", "MODERATOR", "USER"}
        );

        AuthorizationPrincipal principal = mock(AuthorizationPrincipal.class);
        when(provider.authenticate(context)).thenReturn(principal);

        when(principal.hasRole("ADMIN")).thenReturn(false);

        AuthorizationDecision decision = manager.authorize(context, handler);

        assertFalse(decision.isGranted());

        verify(principal).hasRole("ADMIN");
        verify(principal, never()).hasRole("MODERATOR");
        verify(principal, never()).hasRole("USER");
    }

    @Test
    void shouldGrantWhenAuthorizationHasNoRequiredRoles() {
        when(handler.authorization()).thenReturn(authorization);
        when(authorization.value()).thenReturn(new String[0]);

        AuthorizationPrincipal principal = mock(AuthorizationPrincipal.class);
        when(provider.authenticate(context)).thenReturn(principal);

        AuthorizationDecision decision = manager.authorize(context, handler);

        assertTrue(decision.isGranted());

        verify(provider).authenticate(context);
        verifyNoMoreInteractions(principal);
    }

}