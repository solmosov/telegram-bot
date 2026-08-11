package io.github.shahbozolmosov.keyboard.reply;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReplyKeyboardButton(
        String text
) implements ReplyKeyboardElement {

}
