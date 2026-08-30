package io.github.solmosov.telegrambot.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.solmosov.telegrambot.model.InputFile;
import tools.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class MultipartBodyBuilder {

    private static final String LINE = "\r\n";

    private final ObjectMapper objectMapper;

    public MultipartBodyBuilder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public MultipartBody build(Object requestBody) {
        String boundary = "----TelegramBotBoundary" + UUID.randomUUID();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            for (Field field : collectFields(requestBody.getClass())) {
                field.setAccessible(true);
                Object value = field.get(requestBody);

                if (value == null || field.isAnnotationPresent(JsonIgnore.class)) {
                    continue;
                }

                String name = resolveName(field);

                if (value instanceof InputFile file) {
                    writeFilePart(out, boundary, name, file);
                } else {
                    writeTextPart(out, boundary, name, serializeScalarOrJson(value));
                }
            }

            out.write(("--" + boundary + "--" + LINE).getBytes(StandardCharsets.UTF_8));
            return new MultipartBody(boundary, out.toByteArray());
        } catch (IOException | IllegalAccessException ex) {
            throw new RuntimeException("Error creating the multipart body: " + requestBody.getClass(), ex);
        }
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


    private void writeFilePart(ByteArrayOutputStream out, String boundary, String name, InputFile file) throws IOException {
        out.write(("--" + boundary + LINE).getBytes(StandardCharsets.UTF_8));
        out.write(("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + file.getFileName() + "\"" + LINE).getBytes(StandardCharsets.UTF_8));
        out.write(("Content-Type: " + file.getMimeType() + LINE + LINE).getBytes(StandardCharsets.UTF_8));
        out.write(file.getData());
        out.write(LINE.getBytes(StandardCharsets.UTF_8));
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


    private void writeTextPart(ByteArrayOutputStream out, String boundary, String name, String value) throws IOException {
        out.write(("--" + boundary + LINE).getBytes(StandardCharsets.UTF_8));
        out.write(("Content-Disposition: form-data; name=\"" + name + "\"" + LINE + LINE).getBytes(StandardCharsets.UTF_8));
        out.write((value + LINE).getBytes(StandardCharsets.UTF_8));
    }


}
