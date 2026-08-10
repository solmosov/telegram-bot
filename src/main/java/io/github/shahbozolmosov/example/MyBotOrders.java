package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.BotHandler;
import io.github.shahbozolmosov.annotation.CallbackQuery;
import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.InlineKeyboard;

import static io.github.shahbozolmosov.keyboard.InlineKeyboard.button;
import static io.github.shahbozolmosov.keyboard.InlineKeyboard.row;

@BotHandler
public class MyBotOrders {

    @Message("my orders")
    public void myOrders(BotContext context) {
        var inlineKeyboard = InlineKeyboard.of(
                row(
                        button("Order 1", "order:1"),
                        button("Order 2", "order:2"),
                        button("Order 3", "order:3")
                )
        );

        context.sendMessage("Orders", inlineKeyboard);
    }

    @CallbackQuery("order")
    public void order(BotContext context) {
        context.answerCallbackQuery("Order is selected: " + context.callbackParams()[0]);
        context.editMessage("Order " + context.callbackParams()[0]);
    }

}
