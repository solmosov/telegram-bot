package io.github.shahbozolmosov.telegrambot.request.callback;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AnswerCallbackRequest(
        @JsonProperty("callback_query_id")
        String callbackQueryId,

        String text,

        @JsonProperty("show_alert")
        Boolean showAlert
) {

}
