package io.github.shahbozolmosov.context;

import io.github.shahbozolmosov.model.Contact;
import io.github.shahbozolmosov.model.Location;
import io.github.shahbozolmosov.model.Message;

import java.util.Optional;

public final class ReplyKeyboardContext {

    private final MessageContext messageContext;

    public ReplyKeyboardContext(
            MessageContext messageContext
    ) {
        this.messageContext = messageContext;
    }


    public Message message() {
        return messageContext.message();
    }

    public String text() {
        return messageContext.text();
    }

    public Optional<Location> location() {
        return Optional.ofNullable(messageContext.message().location());
    }

    public Contact contact(){
        return messageContext.message().contact();
    }
}
