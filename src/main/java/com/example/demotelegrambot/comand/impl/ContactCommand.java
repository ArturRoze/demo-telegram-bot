package com.example.demotelegrambot.comand.impl;

import com.example.demotelegrambot.comand.Command;
import com.example.demotelegrambot.sender.MessageSender;
import com.example.demotelegrambot.service.bundle.MessageBundleService;
import com.example.demotelegrambot.service.bundle.UserLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;

@Component
public class ContactCommand implements Command<Message> {

    private final MessageSender messageSender;

    private final MessageBundleService messageBundleService;

    @Autowired
    public ContactCommand(MessageSender messageSender,
                          MessageBundleService messageBundleService) {
        this.messageSender = messageSender;
        this.messageBundleService = messageBundleService;
    }

    @Override
    public void execute(Message message) {

        Locale locale = Locale.forLanguageTag(message.getFrom().getLanguageCode());

        SendMessage sendMessage = buildSendMessage(message, UserLocale.valueOf(locale.getLanguage()));

        messageSender.send(sendMessage);

    }

    private SendMessage buildSendMessage(Message message, UserLocale userLocale) {
        return SendMessage.builder()
                .chatId(String.valueOf(message.getChatId()))
                .text(messageBundleService.findLocaleMessage("sand.message.contact", userLocale)
                        .applyPlaceholder("NAME", message.getFrom()
                                .getFirstName())
                        .getText()
                )
                .build();
    }
}
