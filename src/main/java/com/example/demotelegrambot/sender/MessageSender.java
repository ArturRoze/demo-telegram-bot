package com.example.demotelegrambot.sender;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageSender {

    void send(SendMessage sendMessage);
}
