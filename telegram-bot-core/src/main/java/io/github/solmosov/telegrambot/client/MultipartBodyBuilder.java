package io.github.solmosov.telegrambot.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.solmosov.telegrambot.exception.TelegramBotException;
import io.github.solmosov.telegrambot.model.InputFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.ObjectMapper;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

final class MultipartBodyBuilder {

    private static final String LINE = "\r\n";
    private static final Logger log = LoggerFactory.getLogger(MultipartBodyBuilder.class);

    private final ObjectMapper objectMapper;

    public MultipartBodyBuilder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public MultipartBody build(Object requestBody) {
        String boundary = "----TelegramBotBoundary" + UUID.randomUUID().toString().replace("-", "");
        String requestName = requestBody.getClass().getSimpleName();

        log.info("Preparing multipart request body for: {}", requestName);

        try {
            StringBuilder textFields = new StringBuilder();
            List<FilePart> fileParts = new ArrayList<>();

            for (Field field : collectFields(requestBody.getClass())) {
                field.setAccessible(true);
                Object value = field.get(requestBody);


                if (value == null || field.isAnnotationPresent(JsonIgnore.class)) {
                    continue;
                }

                String name = resolveName(field);

                if (value instanceof InputFile file) {
                    long fileSize = Files.size(file.getPath());

                    log.debug("Attaching file field '{}': filename='{}', size={} bytes, path='{}'",name, file.getFileName(), fileSize, file.getPath());


                    String fileHeader = "--" + boundary + LINE
                            + "Content-Disposition: form-data; name=\"" + name + "\"; " + "filename=\"" + file.getFileName() + "\"" + LINE
                            + "Content-Type: " + file.getMimeType() + LINE + LINE;

                    byte[] headerBytes = fileHeader.getBytes(StandardCharsets.UTF_8);
                    byte[] separatorBytes = LINE.getBytes(StandardCharsets.UTF_8);

                    fileParts.add(new FilePart(name, headerBytes, file.getPath(), separatorBytes, fileSize));
                } else {
                    textFields.append("--").append(boundary).append(LINE)
                            .append("Content-Disposition: form-data; name=\"").append(name).append("\"").append(LINE).append(LINE)
                            .append(serializeScalarOrJson(value)).append(LINE);
                }
            }

            HttpRequest.BodyPublisher publisher = getBodyPublisher(requestName, boundary, textFields, fileParts);
            return new MultipartBody(boundary, publisher);
        } catch (IOException | IllegalAccessException ex) {
            log.error("Failed to build multipart body for request: {}", requestName, ex);
            throw new RuntimeException("Error creating the multipart body: " + requestBody.getClass(), ex);
        }
    }

    private static HttpRequest.BodyPublisher getBodyPublisher(String requestName, String boundary, StringBuilder textFields, List<FilePart> fileParts) {
        String footer = "--" + boundary + "--" + LINE;

        byte[] textFieldsBytes = textFields.toString().getBytes(StandardCharsets.UTF_8);
        byte[] footerBytes = footer.getBytes(StandardCharsets.UTF_8);


        long totalLength = textFieldsBytes.length + footerBytes.length;
        for (FilePart part : fileParts) {
            totalLength += part.headerBytes().length + part.fileSize() + part.separatorBytes().length;
        }

        log.info("Multipart body built for {}: total size = {} bytes, files count={}", requestName, totalLength, fileParts.size());

        HttpRequest.BodyPublisher streamPublisher = HttpRequest.BodyPublishers.ofInputStream(() -> {
            log.info("Starting streaming multipart payload for: {}", requestName);

            List<InputStream> streams = new ArrayList<>();
            List<InputStream> openedFileStreams = new ArrayList<>();

            streams.add(new ByteArrayInputStream(textFieldsBytes));

            for (FilePart part : fileParts) {
                try {
                    log.debug("Opening file input stream for field '{}' from path: {}", part.fieldName(), part.filePath());
                    InputStream fileStream = Files.newInputStream(part.filePath());
                    openedFileStreams.add(fileStream);

                    streams.add(new ByteArrayInputStream(part.headerBytes()));
                    streams.add(fileStream);
                    streams.add(new ByteArrayInputStream(part.separatorBytes()));
                } catch (IOException ex) {
                    log.error("Failed to open input stream for field '{}' at path: {} ", part.fieldName(), part.filePath(), ex);
                    throw new TelegramBotException("Failed to open stream for " + part.filePath(), ex);
                }
            }

            streams.add(new ByteArrayInputStream(footerBytes));

            return new SequenceInputStream(Collections.enumeration(streams)) {
                @Override
                public void close() throws IOException {
                    try {
                        super.close();
                    } finally {
                        for (InputStream fileStream : openedFileStreams) {
                            try {
                                fileStream.close();
                            } catch (IOException ex) {
                                log.warn("Failed to close file input stream", ex);
                            }
                        }

                        log.info("Successfully finished streaming and closed all file resources for: {}", requestName);
                    }
                }
            };
        });

        return HttpRequest.BodyPublishers.fromPublisher(streamPublisher, totalLength);
    }

    private record FilePart(String fieldName, byte[] headerBytes, Path filePath, byte[] separatorBytes, long fileSize) {
    }


    private List<Field> collectFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();

        while (type != null && type != Object.class) {
            for (Field f : type.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    fields.add(f);
                }
            }

            type = type.getSuperclass();
        }

        return fields;
    }


    private String resolveName(Field field) {
        JsonProperty annotation = field.getAnnotation(JsonProperty.class);
        if (annotation != null && !annotation.value().isEmpty()) {
            return annotation.value();
        }

        return field.getName();
    }

    private String serializeScalarOrJson(Object value) {
        String json = objectMapper.writeValueAsString(value);
        boolean isScaler = value instanceof String
                || value instanceof Number
                || value instanceof Boolean
                || value.getClass().isEnum();

        if (isScaler && json.length() >= 2 && json.startsWith("\"") && json.endsWith("\"")) {
            return objectMapper.readValue(json, String.class);
        }

        return json;
    }

}
