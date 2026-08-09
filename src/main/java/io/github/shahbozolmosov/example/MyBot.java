package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.context.BotContext;

public class MyBot {
    @Command("/start")
    public void start(BotContext context){
        context.sendMessage("Welcome, Hello World");
    }

    @Command("/help")
    public void help(BotContext context){
        context.sendMessage("Help");
    }

    @Message("hello")
    public void hello(BotContext context) {
        context.sendMessage("hello");
    }

    @Message
    public void anyText(BotContext context){
        System.out.println("-------------------------any hello render");
        context.sendMessage("any hello render");
    }

    public void other(){
        System.out.println("Other method");
    }
}
