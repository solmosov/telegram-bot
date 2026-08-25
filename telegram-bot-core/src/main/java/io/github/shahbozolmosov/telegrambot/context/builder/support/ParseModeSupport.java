package io.github.shahbozolmosov.telegrambot.context.builder.support;

public interface ParseModeSupport<T> {
    T html();
    T markdown();
    T markdownV2();
}
