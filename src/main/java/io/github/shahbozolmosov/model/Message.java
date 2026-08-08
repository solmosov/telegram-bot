package io.github.shahbozolmosov.model;

public record Message(
        long messageId,
        Chat chat,
        String text
) {
}
