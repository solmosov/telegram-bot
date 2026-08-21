package io.github.telegrambot.scanner;

public final class ClassInstanceFactory {

    public Object create(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
