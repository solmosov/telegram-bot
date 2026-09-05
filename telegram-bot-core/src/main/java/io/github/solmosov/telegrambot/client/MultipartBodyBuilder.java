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
                    String fileHeader = "--" + boundary + LINE
                            + "Content-Disposition: form-data; name=\"" + name + "\"; " + "filename=\"" + file.getFileName() + "\"" + LINE
                            + "Content-Type: " + file.getMimeType() + LINE + LINE;

                    byte[] headerBytes = fileHeader.getBytes(StandardCharsets.UTF_8);
                    byte[] separatorBytes = LINE.getBytes(StandardCharsets.UTF_8);
                    long fileSize = Files.size(file.getPath());

                    fileParts.add(new FilePart(headerBytes, file.getPath(), separatorBytes, fileSize));
                } else {
                    textFields.append("--").append(boundary).append(LINE)
                            .append("Content-Disposition: form-data; name=\"").append(name).append("\"").append(LINE).append(LINE)
                            .append(serializeScalarOrJson(value)).append(LINE);
                }
            }

            HttpRequest.BodyPublisher publisher = getBodyPublisher(boundary, textFields, fileParts);
            return new MultipartBody(boundary, publisher);
        } catch (IOException | IllegalAccessException ex) {
            throw new RuntimeException("Error creating the multipart body: " + requestBody.getClass(), ex);
        }
    }

    private static HttpRequest.BodyPublisher getBodyPublisher(String boundary, StringBuilder textFields, List<FilePart> fileParts) {
        String footer = "--" + boundary + "--" + LINE;

        byte[] textFieldsBytes = textFields.toString().getBytes(StandardCharsets.UTF_8);
        byte[] footerBytes = footer.getBytes(StandardCharsets.UTF_8);


        long totalLength = textFieldsBytes.length + footerBytes.length;
        for (FilePart part : fileParts) {
            totalLength += part.headerBytes().length + part.fileSize() + part.separatorBytes().length;
        }

        HttpRequest.BodyPublisher streamPublisher = HttpRequest.BodyPublishers.ofInputStream(() -> {
            List<InputStream> streams = new ArrayList<>();
            List<InputStream> openedFileStreams = new ArrayList<>();

            streams.add(new ByteArrayInputStream(textFieldsBytes));

            for (FilePart part : fileParts) {
                try {
                    InputStream fileStream = Files.newInputStream(part.filePath());
                    openedFileStreams.add(fileStream);

                    streams.add(new ByteArrayInputStream(part.headerBytes()));
                    streams.add(fileStream);
                    streams.add(new ByteArrayInputStream(part.separatorBytes()));
                } catch (IOException ex) {
                    log.error("Failed to open input stream for path: " + part.filePath(), ex);
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

                        log.info("All multipart file streams closed successfully.");
                    }
                }
            };
        });

        return HttpRequest.BodyPublishers.fromPublisher(streamPublisher, totalLength);
    }

    private record FilePart(byte[] headerBytes, Path filePath, byte[] separatorBytes, long fileSize) {
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

//    private void writeFilePart(ByteArrayOutputStream out, String boundary, String name, InputFile file) throws IOException {
//        out.write(("--" + boundary + LINE).getBytes(StandardCharsets.UTF_8));
//        out.write(("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + file.getFileName() + "\"" + LINE).getBytes(StandardCharsets.UTF_8));
//        out.write(("Content-Type: " + file.getMimeType() + LINE + LINE).getBytes(StandardCharsets.UTF_8));
//        out.write(file.getData());
//        out.write(LINE.getBytes(StandardCharsets.UTF_8));
//    }
//

}
