package io.github.solmosov.telegrambot.scanner;

import java.lang.reflect.Constructor;
import java.util.Map;

public final class ClassInstanceFactory {

    private final Map<Class<?>, Object> dependencies;

    public ClassInstanceFactory(Map<Class<?>, Object> dependencies) {
        this.dependencies = dependencies;
    }

    public Object create(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructors()[0];

            Class<?>[] parameterType = constructor.getParameterTypes();

            Object[] arguments = new Object[parameterType.length];

            for (int i = 0; i < parameterType.length; i++) {
                Object dependency = dependencies.get(parameterType[i]);

                if (dependency == null) {
                    throw new IllegalArgumentException(
                            "No dependency found for: " + parameterType[i].getName()
                    );
                }

                arguments[i] = dependency;
            }

            return constructor.newInstance(arguments);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
