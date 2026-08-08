package io.github.shahbozolmosov.scanner;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class ClassScanner {

    public List<Class<?>> scan(String packageName) {
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

        return scanDirectory(directory, packageName);
    }

    private List<Class<?>> scanDirectory(File directory, String packageName) {
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
                                packageName + "." + file.getName()
                        )
                );
                continue;
            }

            String className = file.getName()
                    .replace(".class", "");

            try {
                classes.add(
                        Class.forName(
                                packageName + "." + className
                        )
                );
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

        return classes;
    }
}
