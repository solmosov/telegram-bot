package io.github.shahbozolmosov.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ParseMode {
    HTML("HTML"),
    MARKDOWN("Markdown"),
    MARKDOWN_V2("MarkdownV2");

    private final String value;

    ParseMode(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }
}
