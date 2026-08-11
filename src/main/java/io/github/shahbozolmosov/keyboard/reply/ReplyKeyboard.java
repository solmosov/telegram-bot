package io.github.shahbozolmosov.keyboard.reply;

import io.github.shahbozolmosov.keyboard.ReplyMarkup;
import io.github.shahbozolmosov.keyboard.inline.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

public final class ReplyKeyboard {

    private ReplyKeyboard() {
    }

    public static ReplyKeyboardButton button(String text) {
        return new ReplyKeyboardButton(text);
    }

    public static ReplyKeyboardRow row(
        ReplyKeyboardButton... buttons
    ){
        return new ReplyKeyboardRow(List.of(buttons));
    }

    public static ReplyKeyboardMarkup of(
            ReplyKeyboardElement... elements
    ){
        List<List<ReplyKeyboardButton>> rows = new ArrayList<>();

        for(ReplyKeyboardElement element : elements){
            if(element instanceof ReplyKeyboardButton button){
                rows.add(List.of(button));
            }else if(element instanceof ReplyKeyboardRow(List<ReplyKeyboardButton> buttons)){
                rows.add(buttons);
            }
        }

        return new ReplyKeyboardMarkup(rows);
    }
}
