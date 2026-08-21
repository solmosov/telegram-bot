package io.github.telegrambot.scanner;

public class ApplicationPackageResolver {
    public String resolve() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (!element.getMethodName().equals("main")) {
                continue;
            }

            try {
                Class<?> mainClass = Class.forName(element.getClassName());

                return mainClass.getPackageName();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

        throw new IllegalArgumentException(
                "Main application class was not found"
        );
    }
}
