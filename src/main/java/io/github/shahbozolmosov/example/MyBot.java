package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.BotHandler;
import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.annotation.RequestUsersHandler;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard;
import io.github.shahbozolmosov.keyboard.reply.RequestUsers;
import io.github.shahbozolmosov.model.UsersShared;
import io.github.shahbozolmosov.request.media.SendDocumentRequest;

import static io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard.button;
import static io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard.buttonRequestUsers;

@BotHandler
public class MyBot {

    private static final int SELECT_USERS = 1;
    private static final int SELECT_PREMIUM_USERS = 2;

    @Command("/start")
    public void start(BotContext context) {
        String html = """
                <b>Welcome, %s! 👋</b>
                
                I'm glad to see you here.
                Choose an option below to get started.
                """
                .formatted(context.message().from().firstName());

        var keyboard = ReplyKeyboard.of(
                button("📦 My Orders")
        );

        context.message().sendHtml(html, keyboard);
    }

    @Message("📦 My Orders")
    public void myOrders(BotContext context){

        var document = SendDocumentRequest.builder().document("https://leman.com/wp-content/uploads/2024/03/placeholder-pdf.pdf");

        context.message().sendDocument(document);
    }
}
