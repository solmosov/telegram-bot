package io.github.shahbozolmosov.callback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CallbackParamResolver {
    private CallbackParamResolver() {

    }


    public static String callbackKey(String callbackPattern) {
        if (!callbackPattern.contains("{") && !callbackPattern.contains("}")) {
            return callbackPattern;
        }

        String[] parts = callbackPattern.split(":");

        StringBuilder key = new StringBuilder();

        for (String part : parts) {
            if (key.isEmpty()) {
                key.append(part);
            } else if (part.startsWith("{") && part.endsWith("}")) {
                key.append(":");
                key.append("{}");
            } else {
                key.append(":");
                key.append(part);
            }
        }

        return key.toString();
    }

    public static String updateKey(String value) {
        String[] parts = value.split(":");

        StringBuilder key = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            if (key.isEmpty()) {
                key.append(parts[i]);
            } else if (i % 2 != 0) {
                key.append(":");
                key.append(parts[i]);
            } else {
                key.append(":");
                key.append("{}");
            }
        }

        return key.toString();
    }

    public static Map<String, Object> params(String callbackPattern, String updateData) {

        String[] patternParts = callbackPattern.split(":");
        String[] dataParts = updateData.split(":");

        Map<String, Object> result = new HashMap<>();

        if (patternParts.length != dataParts.length) {
            return result;
        }

        for (int i = 0; i < patternParts.length; i++) {
            String patternPart = patternParts[i];
            String dataPart = dataParts[i];

            if (patternPart.startsWith("{") && patternPart.endsWith("}")) {
                String paramName = patternPart.substring(1, patternPart.length() - 1);
                result.put(paramName, dataPart);
            }
        }

        return result;
    }
}
