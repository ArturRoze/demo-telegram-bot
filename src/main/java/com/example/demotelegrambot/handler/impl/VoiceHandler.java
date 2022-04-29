package com.example.demotelegrambot.handler.impl;

import com.example.demotelegrambot.comand.impl.VoiceCommand;
import com.example.demotelegrambot.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class VoiceHandler implements Handler {

    private final VoiceCommand voiceCommand;

    @Autowired
    public VoiceHandler(VoiceCommand voiceCommand) {
        this.voiceCommand = voiceCommand;
    }

    @Override
    public boolean supports(Update update) {
        return update.hasMessage() && update.getMessage().hasVoice();

    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        voiceCommand.execute(message);
    }
}
