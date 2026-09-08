package io.github.solmosov.telegrambot.model;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

public final class InputFile {

    private final Path path;
    private final String fileName;
    private final String baseName;
    private final String mimeType;

    public InputFile(Path path) {
        this.fileName = path.getFileName().toString();
        this.mimeType = resolveMimeType(path);
        this.baseName = resolveBaseName(fileName);

        this.path = path;
    }

    public InputFile(Path path, String fileName){
        this.fileName = fileName;
        this.mimeType = resolveMimeType(path);
        this.baseName = resolveBaseName(fileName);

        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public String getBaseName() {
        return baseName;
    }

    public String getMimeType() {
        return mimeType;
    }

    private String resolveBaseName(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    private String resolveMimeType(Path path) {
        try {
            String mimeType = Files.probeContentType(path);
            if (mimeType == null) {
                mimeType = URLConnection.guessContentTypeFromName(path.getFileName().toString());
            }

            return (mimeType != null) ? mimeType : "application/octet-stream";
        } catch (IOException ex) {
            return "application/octet-stream";
        }
    }
}
