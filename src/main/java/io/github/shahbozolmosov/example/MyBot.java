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

        context.message().sendHtml(html);
    }


    @Message("request users")
    public void requestUsers(BotContext context) {
        var keyboard = ReplyKeyboard.of(
                buttonRequestUsers(
                        "Select users",
                        RequestUsers.user(SELECT_USERS, 7)
                ),
                buttonRequestUsers(
                        "Select premium users",
                        RequestUsers.userPremium(SELECT_PREMIUM_USERS, 5)
                )
        );

        context.message().sendText("Please share users", keyboard);
    }

    @RequestUsersHandler(SELECT_USERS)
    public void requestUsersHandler(BotContext context) {
        var users = context.replyKeyboard().usersSharedUsers();

        StringBuilder html = new StringBuilder("<b>Received Users</b>\n\n");

        for (UsersShared.User user : users) {
            html.append("· %s %s \n".formatted(user.firstName(), user.username()));
        }

        context.message().sendHtml(html.toString(), ReplyKeyboard.removeKeyboard());
    }

    @RequestUsersHandler(SELECT_PREMIUM_USERS)
    public void requestUsersPremiumHandler(BotContext context) {
        var users = context.replyKeyboard().usersSharedUsers();

        StringBuilder html = new StringBuilder("<b>Received Premium Users</b>\n\n");

        for (UsersShared.User user : users) {
            html.append("· %s %s \n".formatted(user.firstName(), user.username()));
        }

        context.message().sendHtml(html.toString(), ReplyKeyboard.removeKeyboard());
    }
}
