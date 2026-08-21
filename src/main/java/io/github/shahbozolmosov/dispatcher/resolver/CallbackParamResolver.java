package io.github.shahbozolmosov.dispatcher.resolver;

import java.util.HashMap;
import java.util.Map;

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
                String[] paramParts = patternPart.substring(1, patternPart.length() - 1)
                        .split("\\|");
                String paramName = paramParts[0];
                String paramType = paramParts.length > 1 ? paramParts[1] : null;

                Object paramValue = parseValueByType(dataPart, paramType);

                result.put(paramName, paramValue);
            }
        }

        return result;
    }

    private static Object parseValueByType(String dataPart, String paramType) {
        if (paramType == null) return dataPart;

        return switch (paramType) {
            case "boolean" -> Boolean.valueOf(dataPart);
            case "short" -> Short.valueOf(dataPart);
            case "int" -> Integer.valueOf(dataPart);
            case "long" -> Long.valueOf(dataPart);
            case "double" -> Double.valueOf(dataPart);
            case "flout" -> Float.valueOf(dataPart);
            default -> String.valueOf(dataPart);
        };
    }
}
