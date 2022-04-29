package com.example.demotelegrambot.sender.impl;

import com.example.demotelegrambot.exception.TelegramSendMessageException;
import com.example.demotelegrambot.sender.MessageSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MessageSenderImpl extends DefaultAbsSender implements MessageSender {

    @Value("${telegram.bot.token}")
    private String botToken;

    public MessageSenderImpl() {
        super(new DefaultBotOptions());
    }

    @Override
    public void send(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new TelegramSendMessageException(sendMessage.toString(), e);
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}
