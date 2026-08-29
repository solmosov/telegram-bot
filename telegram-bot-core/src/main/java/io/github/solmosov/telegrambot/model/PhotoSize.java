package io.github.solmosov.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PhotoSize(
        @JsonProperty("file_id")
        String fileId,
        @JsonProperty("file_unique_id")
        String fileUniqueId,
        @JsonProperty("file_size")
        Long fileSize,
        int width,
        int height
) {
}
