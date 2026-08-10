package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.BotHandler;
import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.InlineKeyboard;

@BotHandler
public class MyBotHtml {

    @Message("html text")
    public void htmlText(BotContext context) {
        var inlineKeyboard = InlineKeyboard.of(
                InlineKeyboard.button("Button", "button1")
        );

        context.sendHtml(
                """
                        <b>Title</b> \n
                        <i>Italic </i>
                        
                        Hello <tg-emoji emoji-id="5368324170671202286">👍</tg-emoji>
                        
                        <pre>
                            public static void main(String[] args){
                                //...
                            }
                        </pre>
                        """,
                inlineKeyboard
        );
    }

    @Message("markdown text")
    public void markdownText(BotContext context) {
        var inlineKeyboard = InlineKeyboard.of(
                InlineKeyboard.button("Button", "button1")
        );

        context.sendMarkdown(
                """
                *Title*

                _Italic_

                Hello 👍

                ```
                public static void main(String[] args) {
                    //...
                }
                ```
                """,
                inlineKeyboard
        );
    }

    @Message("markdown v2 text")
    public void markdownV2Text(BotContext context) {
        var inlineKeyboard = InlineKeyboard.of(
                InlineKeyboard.button("Button", "button1")
        );

        context.sendMarkdownV2(
                """
                *Title*

                _Italic_

                Hello 👍

                ```
                public static void main(String[] args) {
                    //...
                }
                ```
                """,
                inlineKeyboard
        );
    }
}
