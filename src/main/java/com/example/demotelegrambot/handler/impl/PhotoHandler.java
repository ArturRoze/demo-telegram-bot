package com.example.demotelegrambot.handler.impl;

import com.example.demotelegrambot.comand.impl.PhotoCommand;
import com.example.demotelegrambot.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class PhotoHandler implements Handler {

    private final PhotoCommand photoCommand;

    @Autowired
    public PhotoHandler(PhotoCommand photoCommand) {
        this.photoCommand = photoCommand;
    }

    @Override
    public boolean supports(Update update) {
        return update.hasMessage() && update.getMessage().hasPhoto();
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        photoCommand.execute(message);
    }
}
