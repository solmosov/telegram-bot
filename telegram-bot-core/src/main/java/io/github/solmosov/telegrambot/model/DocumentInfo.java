package io.github.solmosov.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DocumentInfo(
        @JsonProperty("file_name")
        String fileName,

        @JsonProperty("mime_type")
        String mimeType,

        Thumbnail thumbnail,

        @JsonProperty("file_id")
        String fileId,

        @JsonProperty("file_unique_id")
        String fileUniqueId,

        @JsonProperty("file_size")
        int fileSize
) {
}
