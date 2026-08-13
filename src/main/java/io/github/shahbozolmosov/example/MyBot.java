package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.BotHandler;
import io.github.shahbozolmosov.annotation.CallbackQuery;
import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.inline.InlineKeyboard;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard;

import static io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard.button;

@BotHandler
public class MyBot {

    @Command("/start")
    public void start(BotContext context) {
        String html = """
                <b>Welcome, %s! 👋</b>
                
                I'm glad to see you here.
                Choose an option below to get started.
                """
                .formatted(context.message().from().firstName());

        var keyboard = ReplyKeyboard.of(
                button("Orders")
        );

        context.message().sendHtml(html, keyboard);
    }

    @Message("Orders")
    public void orders(BotContext context) {

        var keyboard = InlineKeyboard.of(
                InlineKeyboard.row(
                        InlineKeyboard.button("1", "orders:page:1:sort:desc"),
                        InlineKeyboard.button("2", "orders:page:2:sort:desc"),
                        InlineKeyboard.button("3", "orders:page:3:sort:desc"),
                        InlineKeyboard.button("...", "orders:page:10:sort:desc")
                )
        );

        context.message().sendText("Page 1/10 \n\n", keyboard);
    }


    @CallbackQuery("orders:page:1:sort:desc")
    public void page1(BotContext context) {
        var html = """
                Page 1
                
                <pre>%s</pre>
                """.formatted(context.callbackQuery().data());

        context.message().sendHtml("Static page 1");
    }

    @CallbackQuery("orders:page:{page}:sort:{sort}")
    public void dynamic(BotContext context) {

        var params = context.callbackParams();

        context.message().sendHtml("""
                    <b>Dynamic</b>
                
                   - Page = %s
                   - Sort = %s 
                """.formatted(params.get("page"), params.get("sort")));
    }
}
