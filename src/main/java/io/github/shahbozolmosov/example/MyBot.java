package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.Command;
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

    public void other(){
        System.out.println("Other method");
    }
}
