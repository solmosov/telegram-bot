package io.github.solmosov.telegrambot.dispatcher;

import io.github.solmosov.telegrambot.authorization.AuthorizationDecision;
import io.github.solmosov.telegrambot.authorization.AuthorizationManager;
import io.github.solmosov.telegrambot.context.BotContext;
import io.github.solmosov.telegrambot.dispatcher.resolver.FallbackMessageTypeResolver;
import io.github.solmosov.telegrambot.dispatcher.resolver.MessageTypeResolver;
import io.github.solmosov.telegrambot.exception.authorization.AccessDeniedException;
import io.github.solmosov.telegrambot.handler.Handler;
import io.github.solmosov.telegrambot.model.*;
import io.github.solmosov.telegrambot.registry.Registry;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageUpdateDispatcherTest {


    private static final String BOT_NAME = "mybot".toLowerCase();

    @Test
    void supports_shouldReturnTrue_whenTypeIsMessage() {
        Registry registry = mock(Registry.class);
        AuthorizationManager authorizationManager = mock(AuthorizationManager.class);

        MessageUpdateDispatcher dispatcher = new MessageUpdateDispatcher(
                registry,
                List.of(),
                mock(FallbackMessageTypeResolver.class),
                authorizationManager
        );

        assertTrue(dispatcher.supports(UpdateType.MESSAGE));
    }

    @Test
    void supports_shouldReturnFalse_whenTypeIsNotMessage() {
        Registry registry = mock(Registry.class);
        AuthorizationManager authorizationManager = mock(AuthorizationManager.class);

        MessageUpdateDispatcher dispatcher = new MessageUpdateDispatcher(
                registry,
                List.of(),
                mock(FallbackMessageTypeResolver.class),
                authorizationManager
        );

        assertFalse(dispatcher.supports(UpdateType.CALLBACK_QUERY));
    }

    @Test
    void dispatch_shouldResolveMessageTypeAndHandleRegisteredHandler() {
        Registry registry = mock(Registry.class);
        MessageTypeResolver resolver = mock(MessageTypeResolver.class);
        FallbackMessageTypeResolver fallbackResolver =
                mock(FallbackMessageTypeResolver.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        Handler handler = mock(Handler.class);
        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.message()).thenReturn(message);
        when(resolver.resolve(message))
                .thenReturn(Optional.of(MessageType.TEXT));
        when(message.text()).thenReturn("hello");

        when(registry.find(MessageType.TEXT, "mybothello"))
                .thenReturn(List.of(handler));

        when(authorizationManager.authorize(context, handler))
                .thenReturn(AuthorizationDecision.granted());

        MessageUpdateDispatcher dispatcher = new MessageUpdateDispatcher(
                registry,
                List.of(resolver),
                fallbackResolver,
                authorizationManager
        );

        dispatcher.dispatch(BOT_NAME, update, context);

        verify(registry).find(MessageType.TEXT, "mybothello");
        verify(authorizationManager).authorize(context, handler);
        verify(handler).handle(update, context);

        verifyNoInteractions(fallbackResolver);
    }

    @Test
    void dispatch_shouldUseFallbackResolver_whenNoResolverCanResolveType() {
        Registry registry = mock(Registry.class);
        MessageTypeResolver resolver = mock(MessageTypeResolver.class);
        FallbackMessageTypeResolver fallbackResolver =
                mock(FallbackMessageTypeResolver.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.message()).thenReturn(message);
        when(resolver.resolve(message)).thenReturn(Optional.empty());
        when(fallbackResolver.resolve(message)).thenReturn(MessageType.TEXT);
        when(message.text()).thenReturn("hello");

        when(registry.find(MessageType.TEXT, "mybothello"))
                .thenReturn(List.of());

        MessageUpdateDispatcher dispatcher = new MessageUpdateDispatcher(
                registry,
                List.of(resolver),
                fallbackResolver,
                authorizationManager
        );

        dispatcher.dispatch(BOT_NAME, update, context);

        verify(fallbackResolver).resolve(message);
        verify(registry).find(MessageType.TEXT, "mybothello");
    }

    @Test
    void dispatch_shouldTryResolversInOrder() {
        Registry registry = mock(Registry.class);
        MessageTypeResolver firstResolver = mock(MessageTypeResolver.class);
        MessageTypeResolver secondResolver = mock(MessageTypeResolver.class);
        FallbackMessageTypeResolver fallbackResolver =
                mock(FallbackMessageTypeResolver.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.message()).thenReturn(message);
        when(firstResolver.resolve(message)).thenReturn(Optional.empty());
        when(secondResolver.resolve(message))
                .thenReturn(Optional.of(MessageType.TEXT));

        when(message.text()).thenReturn("hello");
        when(registry.find(MessageType.TEXT, "mybothello"))
                .thenReturn(List.of());

        MessageUpdateDispatcher dispatcher = new MessageUpdateDispatcher(
                registry,
                List.of(firstResolver, secondResolver),
                fallbackResolver,
                authorizationManager
        );

        dispatcher.dispatch(BOT_NAME, update, context);

        verify(firstResolver).resolve(message);
        verify(secondResolver).resolve(message);
        verifyNoInteractions(fallbackResolver);
    }

    @Test
    void dispatch_shouldResolveCommandKeyUsingFirstWord() {
        Registry registry = mock(Registry.class);
        MessageTypeResolver resolver = mock(MessageTypeResolver.class);
        FallbackMessageTypeResolver fallbackResolver =
                mock(FallbackMessageTypeResolver.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.message()).thenReturn(message);
        when(resolver.resolve(message))
                .thenReturn(Optional.of(MessageType.COMMAND));

        when(message.text()).thenReturn("/start hello world");
        when(registry.find(MessageType.COMMAND, "mybot/start"))
                .thenReturn(List.of());

        MessageUpdateDispatcher dispatcher = new MessageUpdateDispatcher(
                registry,
                List.of(resolver),
                fallbackResolver,
                authorizationManager
        );

        dispatcher.dispatch(BOT_NAME, update, context);

        verify(registry).find(MessageType.COMMAND, "mybot/start");
    }

    @Test
    void dispatch_shouldSetDeepLinkParam_forStartCommand() {
        Registry registry = mock(Registry.class);
        MessageTypeResolver resolver = mock(MessageTypeResolver.class);
        FallbackMessageTypeResolver fallbackResolver =
                mock(FallbackMessageTypeResolver.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.message()).thenReturn(message);
        when(resolver.resolve(message))
                .thenReturn(Optional.of(MessageType.COMMAND));

        when(message.text()).thenReturn("/start abc123");
        when(registry.find(MessageType.COMMAND, "mybot/start"))
                .thenReturn(List.of());

        MessageUpdateDispatcher dispatcher = new MessageUpdateDispatcher(
                registry,
                List.of(resolver),
                fallbackResolver,
                authorizationManager
        );

        dispatcher.dispatch(BOT_NAME, update, context);

        verify(context).setDeepLinkParam("abc123");
    }

    @Test
    void dispatch_shouldNotSetDeepLinkParam_forNonStartCommand() {
        Registry registry = mock(Registry.class);
        MessageTypeResolver resolver = mock(MessageTypeResolver.class);
        FallbackMessageTypeResolver fallbackResolver =
                mock(FallbackMessageTypeResolver.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.message()).thenReturn(message);
        when(resolver.resolve(message))
                .thenReturn(Optional.of(MessageType.COMMAND));

        when(message.text()).thenReturn("/help");
        when(registry.find(MessageType.COMMAND, "mybot/help"))
                .thenReturn(List.of());

        MessageUpdateDispatcher dispatcher = new MessageUpdateDispatcher(
                registry,
                List.of(resolver),
                fallbackResolver,
                authorizationManager
        );

        dispatcher.dispatch(BOT_NAME, update, context);

        verify(context, never()).setDeepLinkParam(any());
    }

    @Test
    void dispatch_shouldResolvePhotoKeyUsingCaption() {
        Registry registry = mock(Registry.class);
        MessageTypeResolver resolver = mock(MessageTypeResolver.class);
        FallbackMessageTypeResolver fallbackResolver =
                mock(FallbackMessageTypeResolver.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.message()).thenReturn(message);
        when(resolver.resolve(message))
                .thenReturn(Optional.of(MessageType.PHOTO));

        when(message.caption()).thenReturn("Photo caption");
        when(registry.find(MessageType.PHOTO, "mybotPhoto caption"))
                .thenReturn(List.of());

        MessageUpdateDispatcher dispatcher = new MessageUpdateDispatcher(
                registry,
                List.of(resolver),
                fallbackResolver,
                authorizationManager
        );

        dispatcher.dispatch(BOT_NAME, update, context);

        verify(registry).find(
                MessageType.PHOTO,
                "mybotphoto caption"
        );
    }

    @Test
    void dispatch_shouldResolveLocationKeyUsingReplyMessageText() {
        Registry registry = mock(Registry.class);
        MessageTypeResolver resolver = mock(MessageTypeResolver.class);
        FallbackMessageTypeResolver fallbackResolver =
                mock(FallbackMessageTypeResolver.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        ReplyToMessage replyToMessage = mock(ReplyToMessage.class);

        when(update.message()).thenReturn(message);
        when(resolver.resolve(message))
                .thenReturn(Optional.of(MessageType.LOCATION));

        when(message.replyToMessage()).thenReturn(replyToMessage);
        when(replyToMessage.text()).thenReturn("Send location");

        when(registry.find(
                MessageType.LOCATION,
                "mybotSend location"
        )).thenReturn(List.of());

        MessageUpdateDispatcher dispatcher = new MessageUpdateDispatcher(
                registry,
                List.of(resolver),
                fallbackResolver,
                authorizationManager
        );

        dispatcher.dispatch(BOT_NAME, update, context);

        verify(registry).find(
                MessageType.LOCATION,
                "mybotsend location"
        );
    }

    @Test
    void dispatch_shouldResolveUsersSharedKeyUsingRequestId() {
        Registry registry = mock(Registry.class);
        MessageTypeResolver resolver = mock(MessageTypeResolver.class);
        FallbackMessageTypeResolver fallbackResolver =
                mock(FallbackMessageTypeResolver.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        UsersShared usersShared = new UsersShared(
                List.of(100L, 200L),
                List.of(),
                123
        );

        when(update.message()).thenReturn(message);
        when(resolver.resolve(message))
                .thenReturn(Optional.of(MessageType.USERS_SHARED));

        when(message.usersShared()).thenReturn(usersShared);

        when(registry.find(
                MessageType.USERS_SHARED,
                "mybot123"
        )).thenReturn(List.of());

        MessageUpdateDispatcher dispatcher = new MessageUpdateDispatcher(
                registry,
                List.of(resolver),
                fallbackResolver,
                authorizationManager
        );

        dispatcher.dispatch(BOT_NAME, update, context);

        verify(registry).find(
                MessageType.USERS_SHARED,
                "mybot123"
        );
    }

    @Test
    void dispatch_shouldThrowAccessDeniedException_whenHandlerIsNotAuthorized() {
        Registry registry = mock(Registry.class);
        MessageTypeResolver resolver = mock(MessageTypeResolver.class);
        FallbackMessageTypeResolver fallbackResolver =
                mock(FallbackMessageTypeResolver.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        Handler handler = mock(Handler.class);
        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.message()).thenReturn(message);
        when(resolver.resolve(message))
                .thenReturn(Optional.of(MessageType.TEXT));

        when(message.text()).thenReturn("hello");
        when(registry.find(MessageType.TEXT, "mybothello"))
                .thenReturn(List.of(handler));

        when(authorizationManager.authorize(context, handler))
                .thenReturn(AuthorizationDecision.denied());

        MessageUpdateDispatcher dispatcher = new MessageUpdateDispatcher(
                registry,
                List.of(resolver),
                fallbackResolver,
                authorizationManager
        );

        assertThrows(
                AccessDeniedException.class,
                () -> dispatcher.dispatch(BOT_NAME, update, context)
        );

        verify(handler, never()).handle(any(), any());
    }

    @Test
    void dispatch_shouldHandleAllRegisteredHandlers() {
        Registry registry = mock(Registry.class);
        MessageTypeResolver resolver = mock(MessageTypeResolver.class);
        FallbackMessageTypeResolver fallbackResolver =
                mock(FallbackMessageTypeResolver.class);
        AuthorizationManager authorizationManager =
                mock(AuthorizationManager.class);

        Handler firstHandler = mock(Handler.class);
        Handler secondHandler = mock(Handler.class);

        BotContext context = mock(BotContext.class);
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.message()).thenReturn(message);
        when(resolver.resolve(message))
                .thenReturn(Optional.of(MessageType.TEXT));

        when(message.text()).thenReturn("hello");

        when(registry.find(MessageType.TEXT, "mybothello"))
                .thenReturn(List.of(firstHandler, secondHandler));

        when(authorizationManager.authorize(context, firstHandler))
                .thenReturn(AuthorizationDecision.granted());

        when(authorizationManager.authorize(context, secondHandler))
                .thenReturn(AuthorizationDecision.granted());

        MessageUpdateDispatcher dispatcher = new MessageUpdateDispatcher(
                registry,
                List.of(resolver),
                fallbackResolver,
                authorizationManager
        );

        dispatcher.dispatch(BOT_NAME, update, context);

        verify(firstHandler).handle(update, context);
        verify(secondHandler).handle(update, context);
    }
}