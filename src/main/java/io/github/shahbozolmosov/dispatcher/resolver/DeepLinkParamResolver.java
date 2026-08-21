package io.github.shahbozolmosov.dispatcher.resolver;

import java.util.Map;

public final class DeepLinkParamResolver {

    private DeepLinkParamResolver() {

    }

    public static String param(
            String text
    ) {
        if (text == null || !text.startsWith("/start ")) {
            return null;
        }

        return text.substring("/start ".length());
    }

}
