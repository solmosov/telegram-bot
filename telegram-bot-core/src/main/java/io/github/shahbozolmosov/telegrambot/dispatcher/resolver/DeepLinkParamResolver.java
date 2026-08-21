package io.github.shahbozolmosov.telegrambot.dispatcher.resolver;

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
