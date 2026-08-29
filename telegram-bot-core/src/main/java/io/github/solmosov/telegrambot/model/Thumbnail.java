package io.github.solmosov.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Thumbnail(
        @JsonProperty("file_id")
        String fileId,

        @JsonProperty("file_unique_id")
        String fileUniqueId,

        @JsonProperty("file_size")
        int fileSize,

        int width,
        int height
) {
}
