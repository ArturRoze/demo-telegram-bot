package com.example.demotelegrambot.handler.impl;

import com.example.demotelegrambot.comand.impl.ContactCommand;
import com.example.demotelegrambot.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ContactHandler implements Handler {

    private final ContactCommand contactCommand;

    @Autowired
    public ContactHandler(ContactCommand contactCommand) {
        this.contactCommand = contactCommand;
    }

    @Override
    public boolean supports(Update update) {
        return update.hasMessage() && update.getMessage().hasContact();
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        contactCommand.execute(message);
    }
}
