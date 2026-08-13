package io.github.shahbozolmosov.scanner.resolver;

import io.github.shahbozolmosov.annotation.RequestUsersHandler;
import io.github.shahbozolmosov.handler.Handler;
import io.github.shahbozolmosov.registry.HandlerMapping;
import io.github.shahbozolmosov.registry.Registry;
import io.github.shahbozolmosov.model.MessageType;

import java.lang.reflect.Method;

public class RequestUsersHandlerAnnotationHandlerResolver implements AnnotationHandlerResolver {
    @Override
    public boolean supports(Method method) {
        return method.isAnnotationPresent(RequestUsersHandler.class);
    }

    @Override
    public void register(Method method, Handler handler, Registry registry) {
        RequestUsersHandler requestUsersHandler = method.getAnnotation(RequestUsersHandler.class);

        String key = requestUsersHandler.value() == -1
                ? null
                : String.valueOf(requestUsersHandler.value());

        HandlerMapping registration = new HandlerMapping(
                MessageType.USERS_SHARED,
                key,
                handler
        );

        registry.register(registration);
    }
}
