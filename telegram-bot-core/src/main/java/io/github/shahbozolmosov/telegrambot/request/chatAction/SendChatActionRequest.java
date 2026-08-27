package io.github.shahbozolmosov.telegrambot.request.chatAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public record SendChatActionRequest(
        @JsonProperty("chat_id")
        long chatId,

        Action action
) {

    public enum Action {
        TYPING("typing"),
        UPLOAD_PHOTO("upload_photo"),
        RECORD_VIDEO("record_video"),
        UPLOAD_VIDEO("upload_video"),
        RECORD_VOICE("record_voice"),
        UPLOAD_VOICE("upload_voice"),
        UPLOAD_DOCUMENT("upload_document"),
        CHOOSE_STICKER("choose_sticker"),
        FIND_LOCATION("find_location"),
        RECORD_VIDEO_NOTE("record_video_note"),
        UPLOAD_VIDEO_NOTE("upload_video_note");

        private final String value;

        Action(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

    }
}
