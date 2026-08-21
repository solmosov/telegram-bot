package io.github.telegrambot.scanner;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.invoke.CallSite;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class ClassScanner {

    public List<Class<?>> scan(
            String packageName,
            Class<? extends Annotation> annotation
    ) {
        String path = packageName.replace(".", "/");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        URL resource = classLoader.getResource(path);

        if (resource == null) {
            return List.of();
        }

        File directory = new File(resource.getFile());

        if (!directory.isDirectory()) {
            return List.of();
        }

        return scanDirectory(directory, packageName, annotation);
    }

    private List<Class<?>> scanDirectory(File directory, String packageName, Class<? extends Annotation> annotation) {
        List<Class<?>> classes = new ArrayList<>();

        File[] files = directory.listFiles();

        if (files == null) {
            return classes;
        }

        for (File file : files) {

            if (!file.getName().endsWith(".class")) {
                classes.addAll(
                        scanDirectory(
                                file,
                                packageName + "." + file.getName(),
                                annotation
                        )
                );
                continue;
            }

            String className = file.getName()
                    .substring(0, file.getName().length() - 6);

            try {

                Class<?> clazz = Class.forName(
                        packageName + "." + className
                );

                if (clazz.isAnnotationPresent(annotation)) {
                    classes.add(clazz);
                }
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

        return classes;
    }
}
