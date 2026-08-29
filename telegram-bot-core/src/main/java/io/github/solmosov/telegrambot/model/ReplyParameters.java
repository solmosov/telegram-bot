package io.github.solmosov.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReplyParameters(

        @JsonProperty("message_id")
        long messageId,

        @JsonProperty("chat_id")
        Long chatId,

        @JsonProperty("allow_sending_without_reply")
        Boolean allowSendingWithoutReply,

        @JsonProperty("quote")
        String quote,

        @JsonProperty("quote_parse_mode")
        ParseMode quoteParseMode,

        @JsonProperty("quote_position")
        Integer quotePosition

) {
}
