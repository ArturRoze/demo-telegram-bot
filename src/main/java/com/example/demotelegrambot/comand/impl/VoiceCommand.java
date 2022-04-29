package com.example.demotelegrambot.comand.impl;

import com.example.demotelegrambot.comand.Command;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class VoiceCommand implements Command<Message> {

    @Override
    public void execute(Message message) {


    }
}
