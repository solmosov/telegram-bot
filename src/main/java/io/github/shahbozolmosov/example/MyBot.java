package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.BotHandler;
import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.annotation.RequestUsersHandler;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard;
import io.github.shahbozolmosov.keyboard.reply.RequestUsers;
import io.github.shahbozolmosov.model.UsersShared;

import static io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard.buttonRequestUsers;

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

        context.message().sendHtml(html);
    }


    @Message("request users")
    public void requestUsers(BotContext context) {
        var keyboard = ReplyKeyboard.of(
                buttonRequestUsers(
                        "Select users",
                        RequestUsers.user(12345, 7)
                )
        );

        context.message().sendText("Please share users", keyboard);
    }

    @RequestUsersHandler("12345")
    public void requestUsersHandler(BotContext context) {
        var users = context.replyKeyboard().usersSharedUsers();

        StringBuilder html = new StringBuilder("<b>Received Users</b>\n\n");

        for (UsersShared.User user : users) {
            html.append("· %s %s \n".formatted(user.firstName(), user.username()));
        }

        context.message().sendHtml(html.toString(), ReplyKeyboard.removeKeyboard());
    }
}
